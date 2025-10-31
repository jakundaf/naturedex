package com.naturedex.observation_service.service;

import com.naturedex.observation_service.dto.PresignedUrlRequest;
import com.naturedex.observation_service.dto.PresignedUrlResponse;
import com.naturedex.observation_service.entity.Observation;
import com.naturedex.observation_service.events.ObservationUploadedEvent;
import com.naturedex.observation_service.exception.UserNotFoundException;
import com.naturedex.observation_service.repository.ObservationRepository;
import com.naturedex.observation_service.utils.ObservationStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadService {

    private final S3Presigner s3Presigner;
    private final S3Client s3;
    private final ObservationRepository observationRepository;
    private final KafkaTemplate<String, ObservationUploadedEvent> kafka;
    private static final String TOPIC = "observation-uploaded";

    @Value("${app.aws.s3.bucket}")
    private String bucket;
    @Value("${app.aws.s3.key-prefix}")
    private String keyPrefix;
    @Value("${app.aws.s3.presign-ttl-minutes}")
    private int ttlMinutes;

    @Value("${app.upload.max-bytes:10485760}") // domyślnie 10MB
    private long maxBytes;
    @Value("#{'${app.upload.allowed-content-types:}'.empty ? T(java.util.List).of() : '${app.upload.allowed-content-types}'.split(',')}")
    private List<String> allowedContentTypes;
    private Set<String> allowedTypes;
    @Value("${app.aws.s3.key-prefix:user}") private String keyPrefixRaw;

    @PostConstruct
    void init() {
        this.keyPrefix = keyPrefixRaw.endsWith("/") ? keyPrefixRaw : keyPrefixRaw + "/";
        this.allowedTypes = allowedContentTypes.stream()
                .map(s -> s.trim().toLowerCase())
                .filter(s -> !s.isBlank())
                .collect(Collectors.toUnmodifiableSet());
        log.info("UPLOAD CONFIG -> maxBytes={}, allowedContentTypes={}", maxBytes, allowedTypes);
    }

    public PresignedUrlResponse createPresignedUrl(Jwt jwt, PresignedUrlRequest request) {

        String userId = jwt.getClaim("id");

        // 1) Walidacje wejścia

        if (!allowedContentTypes.contains(request.contentType())) {
            throw new IllegalArgumentException("Unsupported contentType: " + request.contentType());
        }
        if (request.fileSize() == null || request.fileSize() <= 0 || request.fileSize() > maxBytes) {
            throw new IllegalArgumentException("Invalid file size (max " + maxBytes + ")");
        }

        // 2) Tworzenie Observation ze statusem CREATED

        Observation observation = new Observation();
        observation.setUserId(userId);
        observation.setStatus(ObservationStatus.CREATED);
        observation.setContentType(request.contentType());
        observation.setFileSizeBytes(request.fileSize());
        observation.setLatitude(request.lat());
        observation.setLongitude(request.lng());
        observation.setObservedAt(request.takenAt());
        observation.setCreatedAt(LocalDateTime.now());
        observationRepository.save(observation);

        // 3) Klucz S3 - porządkowanie pod userem/obserwacją

        String random = UUID.randomUUID().toString();
        String objectKey = "%s%s/observations/%d/%s".formatted(
                keyPrefix, userId, observation.getId(), random);

        // 4) Budowanie żądania PUT - contentType + metadata

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(objectKey)
                .contentType(request.contentType())
                .metadata(Map.of("observationId", String.valueOf(observation.getId())))
                .build();

        // 5) Podpisany URL ważny N minut

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(ttlMinutes))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presigned = s3Presigner.presignPutObject(presignRequest);

        // 6) Zapisujemy klucz w bazie

        observation.setObjectKey(objectKey);
        observationRepository.save(observation);

        return new PresignedUrlResponse(
                observation.getId(),
                presigned.url().toString(),
                objectKey,
                LocalDateTime.now().plus(Duration.ofMinutes(ttlMinutes))
        );
    }

    public void finalizeUpload(Jwt jwt, Long id){
        String userId = jwt.getClaim("id");
        if (userId == null) throw new UserNotFoundException("Missing 'id' claim");

        Observation observation = observationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new UserNotFoundException("Observation not found or it is not yours."));

        if (observation.getStatus() == ObservationStatus.UPLOADED) return;
        if (observation.getStatus() != ObservationStatus.CREATED) {
            throw new IllegalStateException("Finalize not allowed from status " + observation.getStatus());
        }

        // 1) HEAD - sprawdzamy czy plik faktycznie jest w S3
        HeadObjectResponse head;
        try {
            head = s3.headObject(HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(observation.getObjectKey())
                    .build());
        } catch (NoSuchKeyException e) {
            throw new IllegalStateException("File not found in S3 for key= " + observation.getObjectKey());
        }

        long expected = observation.getFileSizeBytes();
        long actual   = head.contentLength();

        // 2) Walidacje zgodności (przybliżone – HTTP klient mógł zaniżyć/zaokrąglić)
        if (!head.contentType().equalsIgnoreCase(observation.getContentType())) {
            throw new IllegalStateException("MIME mismatch: expected " + observation.getContentType() + " got " + head.contentType());
        }
        if (actual != expected) {
            log.debug("Finalize check size -> expected={}, actual={}", expected, actual);
            throw new IllegalStateException("Size mismatch: expected " + expected + " bytes, got " + actual + " bytes");
        }

        // 3) Zmieniamy status
        observation.setStatus(ObservationStatus.UPLOADED);
        observation.setUpdatedAt(LocalDateTime.now());
        observationRepository.save(observation);

        ObservationUploadedEvent event = new ObservationUploadedEvent(
                observation.getId(),
                observation.getUserId(),
                observation.getObjectKey(),
                observation.getContentType(),
                observation.getFileSizeBytes(),
                observation.getLatitude(),
                observation.getLongitude(),
                observation.getObservedAt(),
                Instant.now(),
                jwt.getTokenValue()
        );

        kafka.send(TOPIC, String.valueOf(observation.getId()), event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Kafka send failed: {}", ex.getMessage());
                    } else {
                        log.info("Kafka send ok, topic: {}, partition: {}, offset: {}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    }
                });
    }
}

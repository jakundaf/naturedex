package com.naturedex.recognition_worker.messaging;

import com.naturedex.recognition_worker.dto.RecognitionResult;
import com.naturedex.recognition_worker.events.ObservationUploadedEvent;
import com.naturedex.recognition_worker.service.RecognitionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ObservationUploadedListener {

    private final RecognitionService recognition;

    public ObservationUploadedListener(RecognitionService recognition){
        this.recognition = recognition;
    }

    @KafkaListener(topics = "observation-uploaded", containerFactory = "recognitionKafkaListenerFactory")
    public void onMessage(ConsumerRecord<String, ObservationUploadedEvent> record, Acknowledgment ack){
        try {

            ObservationUploadedEvent event = record.value();
            String jwt = record.value().jwt();
            RecognitionResult result = recognition.recognize(event);

            recognition.updateObservationStatus(jwt, event.observationId(), result.getSpecies(), result.getConfidence(), result.getSpeciesId());
            recognition.publishRecognizedEvent(event.observationId(), result);

            ack.acknowledge();
            log.info("Kafka - event received, started recognizing...");
        } catch (Exception e){
            log.error("Recognition failed: " + e.getMessage());
        }
    }


}

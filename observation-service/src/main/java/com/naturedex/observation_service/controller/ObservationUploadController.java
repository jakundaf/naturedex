package com.naturedex.observation_service.controller;

import com.naturedex.observation_service.dto.PresignedUrlRequest;
import com.naturedex.observation_service.dto.PresignedUrlResponse;
import com.naturedex.observation_service.service.UploadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/observations")
@RequiredArgsConstructor
public class ObservationUploadController {

    private final UploadService uploadService;

    @PostMapping("/upload-url")
    public PresignedUrlResponse getUploadUrl(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody PresignedUrlRequest request){
        return uploadService.createPresignedUrl(jwt, request);
    }

    @PostMapping("/{id}/finalize")
    public ResponseEntity<Void> finalizeUpload(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id){
        uploadService.finalizeUpload(jwt, id);
        return ResponseEntity.noContent().build();
    }
}

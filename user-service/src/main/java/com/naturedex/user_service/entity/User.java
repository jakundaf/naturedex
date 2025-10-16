package com.naturedex.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

    @Entity
    @Table(name = "users")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class User {

        @Id
        @Column(columnDefinition = "VARCHAR(36)")
        private String id;

        @Column(nullable = false)
        private String username;

        private String email;

        private String avatarUrl;

        private LocalDateTime createdAt;

        private LocalDateTime lastLoginAt;

    }


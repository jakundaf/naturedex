package com.naturedex.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponse {

    private UUID keycloakUuid;
    private String username;
    private String email;
    private String avatarUrl;

}

package com.naturedex.auth_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserRequest {

    private String email;
    private String username;
    private String password;

}

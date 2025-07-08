package com.naturedex.user_service.dto.mapper;

import com.naturedex.user_service.dto.GetUserResponse;
import com.naturedex.user_service.entity.User;

public class UserServiceMapper {

    public static GetUserResponse mapUserEntityToGetUserResponse (User user){

        return GetUserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .keycloakUuid(user.getKeycloakUuid())
                .build();
    }

}

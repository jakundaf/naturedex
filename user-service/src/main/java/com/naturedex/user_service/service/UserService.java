package com.naturedex.user_service.service;

import com.naturedex.user_service.dto.GetUserResponse;
import com.naturedex.user_service.entity.User;
import com.naturedex.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.naturedex.user_service.dto.mapper.UserServiceMapper.mapUserEntityToGetUserResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    public GetUserResponse findOrCreateUser(Jwt jwt) {

        String username = jwt.getClaim("username");
        String email = jwt.getSubject();

        return userRepository.findByUsername(username)
                .map(existing -> {
                    existing.setLastLoginAt(LocalDateTime.now());
                    userRepository.save(existing);
                    return mapUserEntityToGetUserResponse(existing);
                })
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .username(username)
                            .email(email)
                            .createdAt(LocalDateTime.now())
                            .lastLoginAt(LocalDateTime.now())
                            .build();
                    userRepository.save(newUser);

                    return mapUserEntityToGetUserResponse(newUser);
                });
    }
}

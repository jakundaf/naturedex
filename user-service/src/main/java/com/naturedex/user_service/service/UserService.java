package com.naturedex.user_service.service;

import com.naturedex.user_service.dto.CreateUserRequest;
import com.naturedex.user_service.dto.GetUserResponse;
import com.naturedex.user_service.entity.User;
import com.naturedex.user_service.exception.InvalidTokenException;
import com.naturedex.user_service.exception.UserNotFoundException;
import com.naturedex.user_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.naturedex.user_service.dto.mapper.UserServiceMapper.mapUserEntityToGetUserResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public GetUserResponse findOrCreateUser(Jwt jwt) {

        String username = jwt.getClaim("username");
        String email = jwt.getSubject();

        if (username == null || email == null) {
            throw new InvalidTokenException("Missing username or email in JWT token");
        }

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


    public GetUserResponse getUserByUsername(Jwt jwt, String username) {

        return mapUserEntityToGetUserResponse(userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("No user found with passed username")));

    }

    public void createUser(Jwt jwt, CreateUserRequest request) {

        User user = User.builder()
                .email(request.getEmail())
                .username(request.getUsername())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

    }

}

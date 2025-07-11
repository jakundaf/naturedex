package com.naturedex.auth_service.service;

import com.naturedex.auth_service.dto.LoginRequest;
import com.naturedex.auth_service.dto.LoginResponse;
import com.naturedex.auth_service.dto.RegisterRequest;
import com.naturedex.auth_service.entity.User;
import com.naturedex.auth_service.exception.UserAlreadyExistsException;
import com.naturedex.auth_service.exception.UserNotFoundException;
import com.naturedex.auth_service.repository.UserRepository;
import com.naturedex.auth_service.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found. Wrong username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("User not found. Wrong username or password");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getUsername());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);

        return new LoginResponse(token);
    }

    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User with passed email already exists.");
        }

        User user = User.builder().
                email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .createdAt(LocalDate.now())
                .build();

        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getUsername());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);

        HttpEntity<RegisterRequest> entity = new HttpEntity<>(request, headers);
        restTemplate().postForObject("http://user-service:8082/api/users/register", entity, Void.class);

        return new LoginResponse(token);
    }
}

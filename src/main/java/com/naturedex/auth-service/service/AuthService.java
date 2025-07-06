package com.naturedex.auth.service;

import com.naturedex.auth.dto.LoginRequest;
import com.naturedex.auth.dto.LoginResponse;
import com.naturedex.auth.dto.RegisterRequest;
import com.naturedex.auth.entity.User;
import com.naturedex.auth.repository.UserRepository;
import com.naturedex.auth.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest request){
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Wrong username or password."));

        if (passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("Wrong username or password.");
        }

        String token = jwtTokenProvider.generateToken(user.getEmail());

        return new LoginResponse(token);
    }

    public LoginResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("User with passed email already exists.");
        }

        User user = User.builder().
                email(request.getEmail())
                .password(request.getPassword())
                .username(request.getUsername())
                .createdAt(LocalDate.now())
                .build();

        userRepository.save(user);

        String token = jwtTokenProvider.generateToken(user.getEmail());
        return new LoginResponse(token);
    }
}

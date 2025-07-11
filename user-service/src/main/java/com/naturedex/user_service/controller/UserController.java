package com.naturedex.user_service.controller;

import com.naturedex.user_service.dto.CreateUserRequest;
import com.naturedex.user_service.dto.GetUserResponse;
import com.naturedex.user_service.entity.User;
import com.naturedex.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<GetUserResponse> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(userService.findOrCreateUser(jwt), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<GetUserResponse> getUserByUsername(@AuthenticationPrincipal Jwt jwt, String username){
        return new ResponseEntity<>(userService.getUserByUsername(jwt, username), HttpStatus.OK);
    }

    @PostMapping("/register")
    public void createUser(@AuthenticationPrincipal Jwt jwt, @RequestBody CreateUserRequest request){
        userService.createUser(jwt, request);
    }

}

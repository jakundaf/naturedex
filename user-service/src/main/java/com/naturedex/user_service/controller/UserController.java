package com.naturedex.user_service.controller;

import com.naturedex.user_service.dto.GetUserResponse;
import com.naturedex.user_service.entity.User;
import com.naturedex.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<GetUserResponse> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(userService.findOrCreateUser(jwt), HttpStatus.OK);
    }
}

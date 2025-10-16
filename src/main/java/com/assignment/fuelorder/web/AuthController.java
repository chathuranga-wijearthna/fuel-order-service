package com.assignment.fuelorder.web;

import com.assignment.fuelorder.dto.AuthDtos;
import com.assignment.fuelorder.dto.AuthDtos.TokenResponse;
import com.assignment.fuelorder.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Long register(@Valid @RequestBody AuthDtos.RegisterRequest registerRequest) {
        log.debug("Received request to register email {}", registerRequest.email());
        return authService.register(registerRequest);
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody AuthDtos.LoginRequest loginRequest) {
        log.debug("Received request to login email {}", loginRequest.email());
        return authService.login(loginRequest);
    }
}

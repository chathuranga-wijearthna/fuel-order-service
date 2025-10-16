package com.assignment.fuelorder.service;

import com.assignment.fuelorder.dto.AuthDtos.LoginRequest;
import com.assignment.fuelorder.dto.AuthDtos.RegisterRequest;
import com.assignment.fuelorder.dto.AuthDtos.TokenResponse;

public interface AuthService {

    TokenResponse login(LoginRequest loginRequest);

    Long register(RegisterRequest registerRequest);
}

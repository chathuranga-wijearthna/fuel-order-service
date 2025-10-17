package com.assignment.fuelorder.service.impl;

import com.assignment.fuelorder.dto.AuthDtos.LoginRequest;
import com.assignment.fuelorder.dto.AuthDtos.RegisterRequest;
import com.assignment.fuelorder.dto.AuthDtos.TokenResponse;
import com.assignment.fuelorder.entity.User;
import com.assignment.fuelorder.exception.CustomGlobalException;
import com.assignment.fuelorder.exception.ErrorCode;
import com.assignment.fuelorder.repo.UserRepository;
import com.assignment.fuelorder.security.JwtUtil;
import com.assignment.fuelorder.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtUtil jwtUtil;

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        try {
            Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
            String token = jwtUtil.generateToken(auth);
            return new TokenResponse(token);
        } catch (BadCredentialsException e) {
            throw new CustomGlobalException(ErrorCode.BAD_CREDENTIALS);
        }
    }

    @Override
    public Long register(RegisterRequest registerRequest) {

        if (userRepository.existsByEmailIgnoreCase(registerRequest.email())) {
            throw new CustomGlobalException(ErrorCode.EMAIL_ALREADY_REGISTERED);
        }

        User user = new User();
        user.setEmail(registerRequest.email());
        user.setPasswordHash(encoder.encode(registerRequest.password()));
        user.setUserRole(registerRequest.userRole());
        userRepository.save(user);

        return user.getId();
    }
}

package com.assignment.fuelorder.dto;

import com.assignment.fuelorder.dto.constants.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AuthDtos {

    public record RegisterRequest(@NotBlank @Email String email, @NotBlank String password, @NotNull Role userRole) {

    }

    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {

    }

    public record TokenResponse(String token) {

    }
}

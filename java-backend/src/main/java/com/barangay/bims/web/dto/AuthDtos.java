package com.barangay.bims.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {
    public record LoginRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String role
    ) {}

    public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String fullName,
        @NotBlank @Size(min = 6) String password,
        @NotBlank String confirmPassword,
        @NotBlank String role
    ) {}

    public record AuthResponse(
        Long userId,
        String username,
        String role
    ) {}
}


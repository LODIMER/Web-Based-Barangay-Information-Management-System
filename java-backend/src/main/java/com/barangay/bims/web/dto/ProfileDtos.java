package com.barangay.bims.web.dto;

import jakarta.validation.constraints.NotBlank;

public class ProfileDtos {
    public record UpdateProfileRequest(
        @NotBlank String fullName,
        String contactNumber,
        String address
    ) {}
}


package com.barangay.bims.web.dto;

import com.barangay.bims.domain.UrgencyLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class AyudaDtos {
    public record CreateAyudaRequest(
        @NotBlank String requestType,
        @NotNull UrgencyLevel urgencyLevel,
        @NotBlank String description,
        LocalDate preferredDate
    ) {}
}


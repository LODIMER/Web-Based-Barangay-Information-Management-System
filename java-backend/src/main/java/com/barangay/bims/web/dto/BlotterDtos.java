package com.barangay.bims.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class BlotterDtos {
    public record CreateBlotterRequest(
        @NotNull LocalDate incidentDate,
        @NotBlank String type,
        @NotBlank String location,
        @NotBlank String details
    ) {}
}


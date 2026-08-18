package com.locallink.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WorkerOnboardingRequest {

    @NotBlank(message = "Service category is required")
    private String serviceCategory;

    @NotNull(message = "Experience is required")
    @PositiveOrZero(message = "Experience cannot be negative")
    private Integer experienceYears;

    @NotBlank(message = "Bio is required")
    private String bio;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Availability is required")
    private String availability;
}
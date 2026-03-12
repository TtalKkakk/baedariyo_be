package com.house.biet.delivery.query.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record UpdateLocationRequestDto(

        @NotNull
        @DecimalMin("-90.0")
        @DecimalMax("90.0")
        double latitude,

        @NotNull
        @DecimalMin("-180.0")
        @DecimalMax("180.0")
        double longitude
) {
}

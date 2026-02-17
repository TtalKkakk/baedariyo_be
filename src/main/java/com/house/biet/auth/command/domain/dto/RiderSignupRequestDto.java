package com.house.biet.auth.command.domain.dto;

import com.house.biet.common.domain.enums.VehicleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RiderSignupRequestDto(
        @NotBlank
        @Email
        String email,

        @NotBlank
        String password,

        @NotBlank
        String name,

        @NotBlank
        String nickname,

        @NotBlank
        String phoneNumber,

        @NotBlank
        VehicleType vehicleType
) {
}
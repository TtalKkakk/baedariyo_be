package com.house.biet.rider.command.dto;

import com.house.biet.common.domain.enums.VehicleType;
import jakarta.validation.constraints.NotNull;

public record ChangeVehicleTypeRequestDto(

        @NotNull(message = "차량 타입은 필수입니다.")
        VehicleType vehicleType

) {}
package com.house.biet.user.command.dto;

import jakarta.validation.constraints.NotNull;

public record ChangeDefaultAddressRequestDto(

        @NotNull(message = "주소 ID는 필수입니다.")
        String addressAlias
) {}
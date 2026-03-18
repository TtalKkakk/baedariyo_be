package com.house.biet.user.command.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeAddressAliasRequestDto(
        @NotBlank(message = "기존 주소 별명은 필수입니다.")
        String addressAlias,

        @NotBlank(message = "새로운 주소 별명은 필수입니다.")
        String newAddressAlias
) {
}

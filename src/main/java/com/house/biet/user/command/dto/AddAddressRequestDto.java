package com.house.biet.user.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AddAddressRequestDto(

        @NotBlank(message = "도로명 주소는 필수입니다.")
        @Size(max = 100, message = "도로명 주소는 100자 이하여야 합니다.")
        String roadAddress,

        @NotBlank(message = "지번 주소는 필수입니다.")
        @Size(max = 100, message = "지번 주소는 100자 이하여야 합니다.")
        String jibunAddress,

        @NotBlank(message = "상세 주소는 필수입니다.")
        @Size(max = 100, message = "상세 주소는 100자 이하여야 합니다.")
        String detailAddress,

        @NotBlank(message = "주소 별칭은 필수입니다.")
        @Size(max = 20, message = "주소 별칭은 20자 이하여야 합니다.")
        String alias,

        @NotNull(message = "기본 배송지 여부는 필수입니다.")
        Boolean isDefault
) {}
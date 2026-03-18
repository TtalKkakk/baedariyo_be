package com.house.biet.user.command.dto;

import com.house.biet.user.command.domain.entity.UserAddress;

public record AddressResponseDto(
        String alias,
        String roadAddress,
        String jibunAddress,
        String detailAddress,
        boolean isDefault
) {
    public static AddressResponseDto from(UserAddress address) {
        return new AddressResponseDto(
                address.getAlias().getValue(),
                address.getAddress().getRoadAddress(),
                address.getAddress().getJibunAddress(),
                address.getAddress().getDetailAddress(),
                address.isDefault()
        );
    }
}
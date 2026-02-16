package com.house.biet.common.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String roadAddress;     // 필수
    private String jibunAddress;    // 필수
    private String detailAddress;   // 필수

    public Address(String roadAddress, String jibunAddress, String detailAddress) {
        if (roadAddress == null || roadAddress.isBlank()
                || jibunAddress == null || jibunAddress.isBlank()
                || detailAddress == null || detailAddress.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_ADDRESS_FORMAT);
        }

        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.detailAddress = detailAddress;
    }
}
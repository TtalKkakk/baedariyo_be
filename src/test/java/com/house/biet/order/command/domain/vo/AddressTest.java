package com.house.biet.order.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AddressTest {

    @Test
    @DisplayName("성공 - Address 생성")
    void createAddress_Success() {
        // given
        String roadAddress = "서울특별시 마포구 월드컵북로 396";
        String jibunAddress = "서울특별시 마포구 상암동 1605";
        String detailAddress = "101동 1001호";

        // when
        Address address = new Address(roadAddress, jibunAddress, detailAddress);

        // then
        assertThat(address).isNotNull();
        assertThat(address.getRoadAddress()).isEqualTo(roadAddress);
        assertThat(address.getJibunAddress()).isEqualTo(jibunAddress);
        assertThat(address.getDetailAddress()).isEqualTo(detailAddress);
    }

    @Test
    @DisplayName("에러 - roadAddress가 null")
    void createAddress_Error_NullRoadAddress() {
        // given
        String roadAddress = null;
        String jibunAddress = "지번";
        String detailAddress = "상세";

        // when & then
        assertThatThrownBy(() ->
                new Address(roadAddress, jibunAddress, detailAddress))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ADDRESS_FORMAT.getMessage());
    }

    @Test
    @DisplayName("에러 - jibunAddress가 blank")
    void createAddress_Error_BlankJibunAddress() {
        // given
        String roadAddress = "도로명";
        String jibunAddress = " ";
        String detailAddress = "상세";

        // when & then
        assertThatThrownBy(() ->
                new Address(roadAddress, jibunAddress, detailAddress))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ADDRESS_FORMAT.getMessage());
    }

    @Test
    @DisplayName("에러 - detailAddress가 빈 문자열")
    void createAddress_Error_EmptyDetailAddress() {
        // given
        String roadAddress = "도로명";
        String jibunAddress = "지번";
        String detailAddress = "";

        // when & then
        assertThatThrownBy(() ->
                new Address(roadAddress, jibunAddress, detailAddress))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_ADDRESS_FORMAT.getMessage());
    }
}

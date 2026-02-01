package com.house.biet.member.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PhoneNumberTest {

    @Test
    @DisplayName("성공 - PhoneNumber VO 생성")
    void createPhoneNumberVO_Success() {
        // given
        String phoneNumberValue = "010-1111-1111";

        // when
        PhoneNumber phoneNumber = new PhoneNumber(phoneNumberValue);

        // then
        assertThat(phoneNumber).isNotNull();
        assertThat(phoneNumber).isInstanceOf(PhoneNumber.class);
    }

    @Test
    @DisplayName("실패 - PhoneNumber format 에러")
    void createPhoneNumberVO_Error_NotMatchFormat() {
        // given
        String phoneNumberValue = "01088888588";

        // when & then
        assertThatThrownBy(() -> new PhoneNumber(phoneNumberValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_PHONE_NUMBER_FORMAT.getMessage());
    }
}
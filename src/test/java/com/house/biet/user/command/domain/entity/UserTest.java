package com.house.biet.user.command.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("성공 - 유저 생성 성공")
    void CreateUser_Success() {
        // given
        String givenRealNameValue = "<REAL_NAME>";
        String givenNickNameValue = "<NICK_NAME>";
        String givenPhoneNumberValue = "010-1111-1111";

        // when
        User user = User.create(givenRealNameValue, givenNickNameValue, givenPhoneNumberValue);

        // then
        assertThat(user.getRealName().getValue()).isEqualTo(givenRealNameValue);
        assertThat(user.getNickname().getValue()).isEqualTo(givenNickNameValue);
        assertThat(user.getPhoneNumber().getValue()).isEqualTo(givenPhoneNumberValue);
    }
}
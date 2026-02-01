package com.house.biet.user.command.domain.entity;

import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.member.command.domain.vo.PhoneNumber;
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

    @Test
    @DisplayName("성공 - 유저 닉네임 변경 성공")
    void changeNickname_Success() {
        // given
        String givenRealNameValue = "<REAL_NAME>";
        String givenNickNameValue = "<NICK_NAME>";
        String givenPhoneNumberValue = "010-1111-1111";

        User user = User.create(givenRealNameValue, givenNickNameValue, givenPhoneNumberValue);

        Nickname newNickname = new Nickname("<NEW_NICKNAME>");

        // when
        user.changeNickname(newNickname);

        // then
        assertThat(user.getNickname()).isEqualTo(newNickname);
    }
    
    @Test
    @DisplayName("성공 - 유저 휴대전화번호 변경 성공")
    void changePhoneNumber_Success() {
        // given
        String givenRealNameValue = "<REAL_NAME>";
        String givenNickNameValue = "<NICK_NAME>";
        String givenPhoneNumberValue = "010-1111-1111";

        User user = User.create(givenRealNameValue, givenNickNameValue, givenPhoneNumberValue);

        PhoneNumber newPhoneNumber = new PhoneNumber("010-1111-1112");

        // when
        user.changePhoneNumber(newPhoneNumber);

        // then
        assertThat(user.getPhoneNumber()).isEqualTo(newPhoneNumber);
    }
}
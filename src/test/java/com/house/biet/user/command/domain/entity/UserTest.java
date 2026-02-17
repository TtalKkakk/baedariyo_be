package com.house.biet.user.command.domain.entity;

import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.member.command.domain.vo.Password;
import com.house.biet.member.command.domain.vo.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    String givenEmail = "abc@xyz.com";
    String givenPassword = UUID.randomUUID().toString().substring(1, 30);

    String givenRealNameValue = "<REAL_NAME>";
    String givenNickNameValue = "<NICK_NAME>";
    String givenPhoneNumberValue = "010-1111-1111";

    Account account;

    @BeforeEach
    void setup() {
        account = Account.signup(
                new Email(givenEmail),
                Password.encrypt(givenPassword, ENCODER),
                UserRole.USER
        );
    }

    @Test
    @DisplayName("성공 - 유저 생성 성공")
    void CreateUser_Success() {
        // when
        User user = User.create(account, givenRealNameValue, givenNickNameValue, givenPhoneNumberValue);

        // then
        assertThat(user.getRealName().getValue()).isEqualTo(givenRealNameValue);
        assertThat(user.getNickname().getValue()).isEqualTo(givenNickNameValue);
        assertThat(user.getPhoneNumber().getValue()).isEqualTo(givenPhoneNumberValue);
    }

    @Test
    @DisplayName("성공 - 유저 닉네임 변경 성공")
    void changeNickname_Success() {
        // given
        User user = User.create(account, givenRealNameValue, givenNickNameValue, givenPhoneNumberValue);

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
        User user = User.create(account, givenRealNameValue, givenNickNameValue, givenPhoneNumberValue);

        PhoneNumber newPhoneNumber = new PhoneNumber("010-1111-1112");

        // when
        user.changePhoneNumber(newPhoneNumber);

        // then
        assertThat(user.getPhoneNumber()).isEqualTo(newPhoneNumber);
    }
}
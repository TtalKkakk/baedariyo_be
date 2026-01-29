package com.house.biet.user.command.domain.entity;

import com.house.biet.user.command.domain.vo.Email;
import com.house.biet.user.command.domain.vo.Password;
import com.house.biet.user.command.domain.vo.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Test
    @DisplayName("성공 - 유저 생성 성공")
    void CreateUser_Success() {
        // given
        String givenEmailValue = "abc@xyz.com";
        String givenPasswordValue = "a@lkdslkj!slkjxd";

        // when
        User user = User.create(givenEmailValue, givenPasswordValue, ENCODER);

        // then
        assertThat(user.getEmail().getValue()).isEqualTo(givenEmailValue);
        assertThat(user.getPassword().matches(givenPasswordValue, ENCODER)).isTrue();
        assertThat(user.getRole()).isEqualTo(UserRole.USER);
    }
}
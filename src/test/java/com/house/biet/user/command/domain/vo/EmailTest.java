package com.house.biet.user.command.domain.vo;

import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void createEmailVO_success() {
        // given
        String givenEmail = "abc@xyz.com";

        // when
        Email madeEmail = new Email(givenEmail);

        // then
        assertThat(madeEmail.getValue()).isEqualTo(givenEmail);
    }

    @Test
    void createEmailVO_error_InvalidEmailFormat() {
        // given
        String givenEmail1 = "abcxyz.com";
        String givenEmail2 = "abc@xyz,xyz";

        // when & then
        assertThatThrownBy(() -> new Email(givenEmail1))
                .isInstanceOf(Exception.class)
                .hasMessage(ErrorCode.INVALID_EMAIL_FORMAT.getMessage());

        assertThatThrownBy(() -> new Email(givenEmail2))
                .isInstanceOf(Exception.class)
                .hasMessage(ErrorCode.INVALID_EMAIL_FORMAT.getMessage());
    }
}
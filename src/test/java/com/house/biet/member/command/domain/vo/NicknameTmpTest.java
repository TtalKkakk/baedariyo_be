package com.house.biet.member.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.vo.Nickname;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NicknameTmpTest {
    @Test
    @DisplayName("성공 - NickName vo 생성")
    void CreateNicknameVO_Success() {
        // given
        String value5 = "abcde";
        String value30 = "abcdefghijklmnopqrstuvwxyz0123";

        // when
        Nickname nickname5 = new Nickname(value5);
        Nickname nickname30 = new Nickname(value30);

        // then
        assertThat(nickname5.getValue()).isEqualTo(value5);
        assertThat(nickname30.getValue()).isEqualTo(value30);
    }

    @Test
    @DisplayName("실패 - Nickname VO에 not value 및 길이 에러")
    void CreateNicknameVO_Error_NotValue() {
        // given
        String blankValue = "";
        String value4 = "abcd";
        String value31 = "abcdefghijklmnopqrstuvwxyz01234";

        // when & then
        assertThatThrownBy(() -> new Nickname(blankValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_NICK_NAME_FORMAT.getMessage());

        assertThatThrownBy(() -> new Nickname(value4))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_NICK_NAME_FORMAT.getMessage());

        assertThatThrownBy(() -> new Nickname(value31))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_NICK_NAME_FORMAT.getMessage());
    }
}
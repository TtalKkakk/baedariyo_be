package com.house.biet.member.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.vo.RealName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RealNameTest {

    @Test
    @DisplayName("성공 - 실제이름 vo 생성 성공")
    void CreateRealNameVO_Success() {
        // given
        String value = "<REAL_NAME>";

        // when
        RealName realName = new RealName(value);

        // then
        assertThat(realName).isNotNull();
        assertThat(realName).isInstanceOf(RealName.class);
        assertThat(realName.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("실패 - 실제이름 VO에 not value 에러")
    void CreateRealNameVO_Error_NonValue() {
        // given
        String nonValue = null;
        String blankValue = "";

        // when & then
        assertThatThrownBy(() -> new RealName(nonValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_REAL_NAME_FORMAT.getMessage());

        assertThatThrownBy(() -> new RealName(blankValue))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_REAL_NAME_FORMAT.getMessage());
    }
}
package com.house.biet.store.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class StoreThumbnailTest {

    @Test
    @DisplayName("성공 - 정상적인 썸네일 URL로 생성")
    void createStoreThumbnail_success() {
        // given
        String imageUrl = "https://cdn.example.com/store/thumbnail.png";

        // when
        StoreThumbnail thumbnail = new StoreThumbnail(imageUrl);

        // then
        assertThat(thumbnail.getImageUrl()).isEqualTo(imageUrl);
    }

    @Test
    @DisplayName("성공 - 썸네일이 없는 경우(null) 허용")
    void createStoreThumbnail_null_allowed() {
        // when
        StoreThumbnail thumbnail = StoreThumbnail.of(null);

        // then
        assertThat(thumbnail).isNull();
    }

    @Test
    @DisplayName("실패 - 빈 문자열은 허용되지 않음")
    void createStoreThumbnail_blank_throwsException() {
        // given
        String blankUrl = "   ";

        // when & then
        assertThatThrownBy(() -> new StoreThumbnail(blankUrl))
                .isInstanceOf(CustomException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.INVALID_THUMBNAIL_URL_FORMAT);
    }
}

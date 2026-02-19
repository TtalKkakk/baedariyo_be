package com.house.biet.store.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StoreReviewImagesTest {

    @Test
    @DisplayName("create - 성공 - 이미지 4장 이하")
    void create_Success_UnderMaxCount() {
        // given
        List<String> images = List.of("img1", "img2", "img3", "img4");

        // when
        StoreReviewImages storeReviewImages = new StoreReviewImages(images);

        // then
        assertThat(storeReviewImages.getImages()).hasSize(4);
    }

    @Test
    @DisplayName("create - 성공 - null 입력")
    void create_Success_NullInput() {
        // when
        StoreReviewImages storeReviewImages = new StoreReviewImages(null);

        // then
        assertThat(storeReviewImages.getImages()).isEmpty();
    }

    @Test
    @DisplayName("create - 실패 - 이미지 4장 초과")
    void create_Fail_ImageCountExceeded() {
        // given
        List<String> images = List.of("1", "2", "3", "4", "5");

        // when & then
        assertThatThrownBy(() -> new StoreReviewImages(images))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_REVIEW_IMAGE_COUNT.getMessage());
    }
}

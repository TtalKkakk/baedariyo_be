package com.house.biet.store.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StoreReviewCommentTest {

    @Test
    @DisplayName("성공 - 유효한 리뷰 코멘트를 생성한다")
    void create_Success_ValidComment() {
        // given
        String validComment = "delicious";

        // when
        StoreReviewComment comment = new StoreReviewComment(validComment);

        // then
        assertThat(comment.getComment()).isEqualTo(validComment);
    }

    @Test
    @DisplayName("실패 - null 리뷰 코멘트를 생성할 때 에러")
    void create_Error_InvalidStoreReviewCommentFormat_NullInput() {
        // when & then
        assertThatThrownBy(() -> new StoreReviewComment(null))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_REVIEW_COMMENT_FORMAT.getMessage());
    }

    @Test
    @DisplayName("실패 - 공백 리뷰 코멘트를 생성할 때 에러")
    void create_Error_InvalidStoreReviewCommentFormat_BlankInput() {
        // given
        String blankComment = "  ";

        // when & then
        assertThatThrownBy(() -> new StoreReviewComment(blankComment))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_REVIEW_COMMENT_FORMAT.getMessage());
    }

    @Test
    @DisplayName("실패 - 너무 긴 리뷰 코멘트를 생성할 때 에러")
    void create_Error_InvalidStoreReviewCommentFormat_TooLongInput() {
        // given
        String tooLong = "a".repeat(151);

        // when & then
        assertThatThrownBy(() -> new StoreReviewComment(tooLong))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_REVIEW_COMMENT_FORMAT.getMessage());
    }

    @Test
    @DisplayName("성공 - null 입력으로 of를 호출하면 null을 반환한다")
    void of_Success_NullInput() {
        // when
        StoreReviewComment comment = StoreReviewComment.of(null);

        // then
        assertThat(comment).isNull();
    }

    @Test
    @DisplayName("성공 - 유효한 입력으로 of를 호출하면 리뷰 코멘트를 반환한다")
    void of_Success_ValidInput() {
        // given
        String validComment = "good";

        // when
        StoreReviewComment comment = StoreReviewComment.of(validComment);

        // then
        assertThat(comment).isNotNull();
        assertThat(comment.getComment()).isEqualTo(validComment);
    }

    @Test
    @DisplayName("실패 - 너무 긴 입력으로 of를 호출할 때 에러")
    void of_Error_InvalidStoreReviewCommentFormat_TooLongInput() {
        // given
        String tooLong = "a".repeat(151);

        // when & then
        assertThatThrownBy(() -> StoreReviewComment.of(tooLong))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_REVIEW_COMMENT_FORMAT.getMessage());
    }
}
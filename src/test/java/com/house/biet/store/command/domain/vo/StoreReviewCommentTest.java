package com.house.biet.store.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StoreReviewCommentTest {

    @Test
    @DisplayName("성공 - 정상 길이 comment")
    void create_Success_ValidComment() {
        String validComment = "맛있어요!";
        StoreReviewComment comment = new StoreReviewComment(validComment);
        assertThat(comment.getComment()).isEqualTo(validComment);
    }

    @Test
    @DisplayName(" 실패 - null 입력")
    void create_Fail_NullInput() {
        assertThatThrownBy(() -> new StoreReviewComment(null))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_REVIEW_COMMENT_FORMAT.getMessage());
    }

    @Test
    @DisplayName("실패 - blank 입력")
    void create_Fail_BlankInput() {
        String blankComment = "  ";
        assertThatThrownBy(() -> new StoreReviewComment(blankComment))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_REVIEW_COMMENT_FORMAT.getMessage());
    }

    @Test
    @DisplayName("실패 - 150자 초과 입력")
    void create_Fail_TooLongInput() {
        String tooLong = "a".repeat(151);
        assertThatThrownBy(() -> new StoreReviewComment(tooLong))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_REVIEW_COMMENT_FORMAT.getMessage());
    }

    @Test
    @DisplayName("성공 - null 입력")
    void of_Success_NullInput() {
        StoreReviewComment comment = StoreReviewComment.of(null);
        assertThat(comment).isNull();
    }

    @Test
    @DisplayName("성공 - 정상 입력")
    void of_Success_ValidInput() {
        String validComment = "좋아요";
        StoreReviewComment comment = StoreReviewComment.of(validComment);
        assertThat(comment).isNotNull();
        assertThat(comment.getComment()).isEqualTo(validComment);
    }

    @Test
    @DisplayName("실패 - 150자 초과 입력")
    void of_Fail_TooLongInput() {
        String tooLong = "a".repeat(151);
        assertThatThrownBy(() -> StoreReviewComment.of(tooLong))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_STORE_REVIEW_COMMENT_FORMAT.getMessage());
    }
}

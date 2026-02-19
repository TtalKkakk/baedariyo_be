package com.house.biet.store.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreReviewComment {

    private static final int MAX_LENGTH = 150;

    private String comment;

    public StoreReviewComment(String comment) {
        if (comment == null || comment.isBlank())
            throw new CustomException(ErrorCode.INVALID_STORE_REVIEW_COMMENT_FORMAT);

        validate(comment);

        this.comment = comment;
    }

    public static StoreReviewComment of(String comment) {
        if (comment == null)
            return null;

        validate(comment);

        return new StoreReviewComment(comment);
    }

    private static void validate(String comment) {
        if (comment.length() > MAX_LENGTH)
            throw new CustomException(ErrorCode.INVALID_STORE_REVIEW_COMMENT_FORMAT);
    }
}

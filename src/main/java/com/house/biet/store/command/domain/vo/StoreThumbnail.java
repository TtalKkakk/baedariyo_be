package com.house.biet.store.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class StoreThumbnail {

    private String imageUrl;

    public StoreThumbnail(String imageUrl) {
        if (imageUrl != null && imageUrl.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_THUMBNAIL_URL_FORMAT);
        }

        this.imageUrl = imageUrl;
    }

    public static StoreThumbnail of(String imageUrl) {
        if (imageUrl == null)
            return null;

        return new StoreThumbnail(imageUrl);
    }
}



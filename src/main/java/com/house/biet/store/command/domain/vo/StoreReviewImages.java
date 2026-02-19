package com.house.biet.store.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class StoreReviewImages {

    private static final int MAX_IMAGE_COUNT = 4;

    @ElementCollection
    @CollectionTable(
            name = "store_review_images",
            joinColumns = @JoinColumn(name = "store_review_id")
    )
    private List<String> images = new ArrayList<>();

    public StoreReviewImages(List<String> images) {
        if (images == null)
            return;

        if (images.size() > MAX_IMAGE_COUNT)
            throw new CustomException(ErrorCode.INVALID_STORE_REVIEW_IMAGE_COUNT);

        this.images.addAll(images);
    }
}

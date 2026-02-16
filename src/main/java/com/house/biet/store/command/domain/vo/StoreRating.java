package com.house.biet.store.command.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class StoreRating {

    private int totalRating;
    private int reviewCount;

    public static StoreRating empty() {
        return new StoreRating(0, 0);
    }

    public StoreRating addRating(int newRating) {
        return new StoreRating(
                totalRating + newRating,
                reviewCount + 1
        );
    }

    public double average() {
        return reviewCount == 0
                ? 0.0
                : (double) totalRating / reviewCount;
    }
}

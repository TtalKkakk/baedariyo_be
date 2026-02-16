package com.house.biet.fixtures;

import com.house.biet.store.command.domain.vo.StoreRating;

public class StoreRatingFixture {

    public static StoreRating empty() {
        return StoreRating.empty();
    }

    public static StoreRating rating(int total, int count) {
        return new StoreRating(total, count);
    }

    public static StoreRating ratedBy(int... ratings) {
        StoreRating rating = StoreRating.empty();
        for (int r : ratings) {
            rating = rating.addRating(r);
        }
        return rating;
    }
}

package com.house.biet.store.command.application;

import com.house.biet.store.command.domain.aggregate.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreRatingService {

    private final StoreService storeService;

    @Transactional
    public void increaseRating(UUID storePublicId, int rating) {
        Store store = storeService.getStoreByPublicId(storePublicId);

        store.addRating(rating);
    }

    @Transactional
    public void decreaseRating(UUID storePublicId, int rating) {
        Store store = storeService.getStoreByPublicId(storePublicId);

        store.removeRating(rating);
    }
}

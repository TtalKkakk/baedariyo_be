package com.house.biet.store.command.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.domain.aggregate.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StoreRatingServiceTest {

    @Mock
    private StoreService storeService;

    @Mock
    private Store store;

    @InjectMocks
    private StoreRatingService storeRatingService;

    private final UUID storePublicId = UUID.randomUUID();

    /* =========================
       increaseRating
       ========================= */

    @Test
    @DisplayName("성공 - 평점 증가")
    void increaseRating_Success() {

        // given
        given(storeService.getStoreByPublicId(storePublicId)).willReturn(store);

        // when
        storeRatingService.increaseRating(storePublicId, 5);

        // then
        verify(storeService).getStoreByPublicId(storePublicId);
        verify(store).addRating(5);
    }

    /* =========================
       decreaseRating
       ========================= */

    @Test
    @DisplayName("성공 - 평점 감소")
    void decreaseRating_Success() {

        // given
        given(storeService.getStoreByPublicId(storePublicId)).willReturn(store);

        // when
        storeRatingService.decreaseRating(storePublicId, 4);

        // then
        verify(storeService).getStoreByPublicId(storePublicId);
        verify(store).removeRating(4);
    }
}
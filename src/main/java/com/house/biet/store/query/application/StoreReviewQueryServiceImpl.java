package com.house.biet.store.query.application;

import com.house.biet.store.query.StoreReviewQueryRepository;
import com.house.biet.store.query.dto.MyStoreReviewDto;
import com.house.biet.store.query.dto.StoreReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StoreReviewQueryServiceImpl implements StoreReviewQueryService {

    private final StoreReviewQueryRepository storeReviewQueryRepository;

    @Override
    public List<MyStoreReviewDto> findMyReviews(Long userId) {
        return storeReviewQueryRepository.findMyReviews(userId);
    }

    @Override
    public List<StoreReviewDto> findReviewsByStore(UUID storePublicId) {
        return storeReviewQueryRepository.findReviewsByStore(storePublicId);
    }

    @Override
    public List<StoreReviewDto> findTop3PhotoReviewsByStore(UUID storePublicId) {
        return storeReviewQueryRepository.findTop3PhotoReviewsByStore(storePublicId);
    }
}

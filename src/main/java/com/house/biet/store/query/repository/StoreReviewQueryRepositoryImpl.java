package com.house.biet.store.query.repository;

import com.house.biet.store.query.StoreReviewQueryRepository;
import com.house.biet.store.query.dto.MyStoreReviewDto;
import com.house.biet.store.query.dto.StoreReviewDto;
import com.house.biet.store.command.domain.entity.StoreReview;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.house.biet.store.command.domain.aggregate.QStore.store;
import static com.house.biet.store.command.domain.entity.QStoreReview.storeReview;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StoreReviewQueryRepositoryImpl implements StoreReviewQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MyStoreReviewDto> findMyReviews(Long userId) {
        // StoreReview + Store join
        List<StoreReview> reviews = queryFactory
                .selectFrom(storeReview)
                .join(store).on(store.publicId.eq(storeReview.storePublicId))
                .where(storeReview.userId.eq(userId))
                .orderBy(storeReview.createdAt.desc())
                .fetch();

        // DTO 변환
        return reviews.stream()
                .map(r -> new MyStoreReviewDto(
                        r.getPublicId(),
                        r.getStorePublicId(),
                        queryFactory.select(store.storeName.value)
                                .from(store)
                                .where(store.publicId.eq(r.getStorePublicId()))
                                .fetchOne(),
                        r.getRating(),
                        r.getCreatedAt(),
                        r.getStoreReviewImages() != null ? r.getStoreReviewImages().getImages() : List.of(),
                        r.getStoreReviewComment() != null ? r.getStoreReviewComment().getComment() : null
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<StoreReviewDto> findReviewsByStore(UUID storePublicId) {
        List<StoreReview> reviews = queryFactory
                .selectFrom(storeReview)
                .join(store).on(store.publicId.eq(storeReview.storePublicId))
                .where(storeReview.storePublicId.eq(storePublicId))
                .orderBy(storeReview.createdAt.desc())
                .fetch();

        return reviews.stream()
                .map(r -> new StoreReviewDto(
                        r.getPublicId(),
                        r.getUserId(),
                        r.getStorePublicId(),
                        queryFactory.select(store.storeName.value)
                                .from(store)
                                .where(store.publicId.eq(r.getStorePublicId()))
                                .fetchOne(),
                        r.getRating(),
                        r.getCreatedAt(),
                        r.getStoreReviewImages() != null ? r.getStoreReviewImages().getImages() : List.of(),
                        r.getStoreReviewComment() != null ? r.getStoreReviewComment().getComment() : null
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<StoreReviewDto> findTop3PhotoReviewsByStore(UUID storePublicId) {
        List<StoreReview> reviews = queryFactory
                .selectFrom(storeReview)
                .join(store).on(store.publicId.eq(storeReview.storePublicId))
                .where(storeReview.storePublicId.eq(storePublicId))
                .orderBy(storeReview.createdAt.desc())
                .limit(3) // 최근 3개만
                .fetch();

        return reviews.stream()
                .map(r -> new StoreReviewDto(
                        r.getPublicId(),
                        r.getUserId(),
                        r.getStorePublicId(),
                        queryFactory.select(store.storeName.value)
                                .from(store)
                                .where(store.publicId.eq(r.getStorePublicId()))
                                .fetchOne(),
                        r.getRating(),
                        r.getCreatedAt(),
                        r.getStoreReviewImages() != null ? r.getStoreReviewImages().getImages() : List.of(),
                        r.getStoreReviewComment() != null ? r.getStoreReviewComment().getComment() : null
                ))
                .collect(Collectors.toList());
    }
}

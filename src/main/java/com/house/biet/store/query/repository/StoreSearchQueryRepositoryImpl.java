package com.house.biet.store.query.repository;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.store.command.domain.aggregate.QStore;
import com.house.biet.store.query.StoreSearchQueryRepository;
import com.house.biet.store.query.dto.StoreSearchQueryDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StoreSearchQueryRepositoryImpl implements StoreSearchQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QStore store = QStore.store;

    @Override
    public List<StoreSearchQueryDto> searchStores(
            String keyword,
            StoreCategory storeCategory,
            double latitude,
            double longitude,
            int offset,
            int size
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        if (keyword != null && !keyword.isBlank()) {
            builder.and(store.storeName.value.containsIgnoreCase(keyword));
        }

        if (storeCategory != null) {
            builder.and(store.storeCategory.eq(storeCategory));
        }

        NumberExpression<Double> distance =
                Expressions.numberTemplate(
                        Double.class,
                        """
                        6371 * acos(
                            cos(radians({0}))
                            * cos(radians({1}))
                            * cos(radians({2}) - radians({3}))
                            + sin(radians({0}))
                            * sin(radians({1}))
                        )
                        """,
                        latitude,
                        store.storeGeoLocation.latitude,
                        store.storeGeoLocation.longitude,
                        longitude
                );

        NumberExpression<Double> averageRating =
                Expressions.numberTemplate(
                        Double.class,
                        "CASE WHEN {0} = 0 THEN 0 ELSE {1} / {0} END",
                        store.rating.reviewCount,
                        store.rating.totalRating
                );

        return queryFactory
                .select(Projections.constructor(
                        StoreSearchQueryDto.class,
                        store.publicId,
                        store.thumbnail.imageUrl,
                        store.storeName.value,
                        store.storeGeoLocation.latitude,
                        store.storeGeoLocation.longitude,
                        store.minimumOrderAmount.amount,
                        averageRating,
                        store.rating.reviewCount,
                        distance
                ))
                .from(store)
                .where(builder)
                .orderBy(distance.asc())  // 거리순 정렬
                .offset(offset)
                .limit(size)
                .fetch();
    }
}

package com.house.biet.store.query;

import com.house.biet.fixtures.StoreFixture;
import com.house.biet.fixtures.StoreReviewFixture;
import com.house.biet.store.command.StoreRepository;
import com.house.biet.store.command.StoreReviewRepository;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.entity.StoreReview;
import com.house.biet.store.command.repository.StoreRepositoryJpaAdapter;
import com.house.biet.store.command.repository.StoreReviewRepositoryJpaAdapter;
import com.house.biet.store.query.dto.MyStoreReviewDto;
import com.house.biet.store.query.dto.StoreReviewDto;
import com.house.biet.store.query.repository.StoreReviewQueryRepositoryImpl;
import com.house.biet.support.config.QueryDslTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        StoreReviewQueryRepositoryImpl.class,
        StoreReviewRepositoryJpaAdapter.class,
        StoreRepositoryJpaAdapter.class,
        QueryDslTestConfig.class
})
@ActiveProfiles("test")
class StoreReviewQueryRepositoryTest {

    @Autowired
    private StoreReviewQueryRepository storeReviewQueryRepository;

    @Autowired
    private StoreReviewRepository storeReviewRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    @DisplayName("성공 - userId 기준 내가 작성한 리뷰 목록 조회")
    void findMyReviews_Success() {
        // given
        Long userId = 100L;

        Store savedStore = storeRepository.save(StoreFixture.createStore());
        UUID storePublicId = savedStore.getPublicId();

        storeReviewRepository.save(
                StoreReviewFixture.createWithStoreIdAndUserId(storePublicId, userId));
        storeReviewRepository.save(
                StoreReviewFixture.createWithStoreIdAndUserId(storePublicId, userId));
        storeReviewRepository.save(
                StoreReviewFixture.create()); // 다른 사용자

        // when
        List<MyStoreReviewDto> results =
                storeReviewQueryRepository.findMyReviews(userId);

        // then
        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(MyStoreReviewDto::getStorePublicId)
                .containsExactlyInAnyOrder(storePublicId, storePublicId);
    }

    @Test
    @DisplayName("성공 - storePublicId 기준 가게 리뷰 목록 조회")
    void findReviewsByStore_Success() {
        // given
        Store savedStore = storeRepository.save(StoreFixture.createStore());
        UUID storePublicId = savedStore.getPublicId();

        storeReviewRepository.save(
                StoreReviewFixture.createWithStoreIdAndUserId(storePublicId, 1L));
        storeReviewRepository.save(
                StoreReviewFixture.createWithStoreIdAndUserId(storePublicId, 2L));
        storeReviewRepository.save(
                StoreReviewFixture.create()); // 다른 가게

        // when
        List<StoreReviewDto> results =
                storeReviewQueryRepository.findReviewsByStore(storePublicId);

        // then
        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(StoreReviewDto::getUserId)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    @DisplayName("성공 - storePublicId 기준 가게 리뷰 목록 조회 | 결과 없음")
    void findReviewsByStore_Success_EmptyResult() {
        // given
        Store savedStore = storeRepository.save(StoreFixture.createStore());
        UUID storePublicId = savedStore.getPublicId();

        // when
        List<StoreReviewDto> results =
                storeReviewQueryRepository.findReviewsByStore(storePublicId);

        // then
        assertThat(results).isEmpty();
    }

    @Test
    @DisplayName("성공 - storePublicId 기준 가게 최근 3개 사진 리뷰 조회")
    void findTop3PhotoReviewsByStore_Success() {
        // given
        Store savedStore = storeRepository.save(StoreFixture.createStore());
        UUID storePublicId = savedStore.getPublicId();

        StoreReview review1 = StoreReviewFixture.createWithStoreId(storePublicId);
        StoreReview review2 = StoreReviewFixture.createWithStoreId(storePublicId);
        StoreReview review3 = StoreReviewFixture.createWithStoreId(storePublicId);
        StoreReview review4 = StoreReviewFixture.createWithStoreId(storePublicId); // 가장 최근

        // 명확한 시간 세팅 (오름차순)
        ReflectionTestUtils.setField(review1, "createdAt",
                LocalDateTime.of(2024, 1, 1, 10, 0));
        ReflectionTestUtils.setField(review2, "createdAt",
                LocalDateTime.of(2024, 1, 1, 11, 0));
        ReflectionTestUtils.setField(review3, "createdAt",
                LocalDateTime.of(2024, 1, 1, 12, 0));
        ReflectionTestUtils.setField(review4, "createdAt",
                LocalDateTime.of(2024, 1, 1, 13, 0));

        storeReviewRepository.save(review1);
        storeReviewRepository.save(review2);
        storeReviewRepository.save(review3);
        storeReviewRepository.save(review4);

        // when
        List<StoreReviewDto> results =
                storeReviewQueryRepository.findTop3PhotoReviewsByStore(storePublicId);

        // then
        assertThat(results).hasSize(3);

        // 최신순 정렬 확인 (13시 → 12시 → 11시)
        assertThat(results.get(0).getCreatedAt())
                .isAfter(results.get(1).getCreatedAt());
        assertThat(results.get(1).getCreatedAt())
                .isAfter(results.get(2).getCreatedAt());

        // 가장 최근(13시) 리뷰가 포함되어 있는지 확인
        assertThat(results.get(0).getCreatedAt())
                .isEqualTo(LocalDateTime.of(2024, 1, 1, 13, 0));

        // 모두 사진이 있는 리뷰인지 확인
        assertThat(results)
                .allSatisfy(r -> assertThat(r.getStoreReviewImages()).isNotEmpty());
    }

    @Test
    @DisplayName("성공 - storePublicId 기준 가게 최근 3개 사진 리뷰 조회 | 결과 없음")
    void findTop3PhotoReviewsByStore_Success_EmptyResult() {
        // given
        Store savedStore = storeRepository.save(StoreFixture.createStore());
        UUID storePublicId = savedStore.getPublicId();

        // when
        List<StoreReviewDto> results =
                storeReviewQueryRepository.findTop3PhotoReviewsByStore(storePublicId);

        // then
        assertThat(results).isEmpty();
    }
}

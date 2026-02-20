package com.house.biet.store.command;


import com.house.biet.fixtures.StoreReviewFixture;
import com.house.biet.store.command.domain.entity.StoreReview;
import com.house.biet.store.command.repository.StoreReviewRepositoryJpaAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(StoreReviewRepositoryJpaAdapter.class)
@ActiveProfiles("test")
class StoreReviewRepositoryTest {

    @Autowired
    private StoreReviewRepository storeReviewRepository;

    @Test
    @DisplayName("성공 - StoreReview 저장")
    void save_Success() {
        // given
        StoreReview review = StoreReviewFixture.create();

        // when
        StoreReview saved = storeReviewRepository.save(review);

        // then
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("성공 - publicStoreReviewId 기준 StoreReview 조회")
    void findByPublicId_Success() {
        // given
        UUID publicStoreReviewId = UUID.randomUUID();
        StoreReview review =
                StoreReviewFixture.createWithPublicId(publicStoreReviewId);
        storeReviewRepository.save(review);

        // when
        Optional<StoreReview> found =
                storeReviewRepository.findByPublicId(publicStoreReviewId);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getPublicId())
                .isEqualTo(publicStoreReviewId);
    }

    @Test
    @DisplayName("실패 - publicStoreReviewId 기준 StoreReview 조회 | 결과 없음")
    void findByPublicId_Error_NotFound() {
        // given
        UUID publicStoreReviewId = UUID.randomUUID();

        // when
        Optional<StoreReview> found =
                storeReviewRepository.findByPublicId(publicStoreReviewId);

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("성공 - publicStoreReviewId 기준 StoreReview 삭제")
    void deleteByPublicId_Success() {
        // given
        UUID publicStoreReviewId = UUID.randomUUID();
        StoreReview review =
                StoreReviewFixture.createWithPublicId(publicStoreReviewId);
        storeReviewRepository.save(review);

        // when
        storeReviewRepository.deleteByPublicId(publicStoreReviewId);

        // then
        assertThat(
                storeReviewRepository.findByPublicId(publicStoreReviewId)
        ).isEmpty();
    }

    @Test
    @DisplayName("성공 - storePublicId 기준 StoreReview 목록 조회")
    void findByStorePublicId_Success() {
        // given
        UUID storePublicId = UUID.randomUUID();
        storeReviewRepository.save(
                StoreReviewFixture.createWithStoreId(storePublicId));
        storeReviewRepository.save(
                StoreReviewFixture.createWithStoreId(storePublicId));
        storeReviewRepository.save(
                StoreReviewFixture.create());

        // when
        List<StoreReview> reviews =
                storeReviewRepository.findByStorePublicId(storePublicId);

        // then
        assertThat(reviews).hasSize(2);
        assertThat(reviews)
                .extracting(StoreReview::getStorePublicId)
                .containsOnly(storePublicId);
    }

    @Test
    @DisplayName("성공 - userId 기준 StoreReview 목록 조회")
    void findByUserId_Success() {
        // given
        Long userId = 999999999999999999L;
        storeReviewRepository.save(
                StoreReviewFixture.createWithUserId(userId));
        storeReviewRepository.save(
                StoreReviewFixture.createWithUserId(userId));
        storeReviewRepository.save(
                StoreReviewFixture.create());

        // when
        List<StoreReview> reviews =
                storeReviewRepository.findByUserId(userId);

        // then
        assertThat(reviews).hasSize(2);
        assertThat(reviews)
                .extracting(StoreReview::getUserId)
                .containsOnly(userId);
    }
}

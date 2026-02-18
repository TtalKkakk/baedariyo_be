package com.house.biet.store.command;

import com.house.biet.fixtures.StoreFixture;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.repository.StoreRepositoryJpaAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(StoreRepositoryJpaAdapter.class)
@ActiveProfiles("test")
class StoreRepositoryTest {

    @Autowired
    StoreRepository storeRepository;

    @Test
    @DisplayName("성공 - Store 저장")
    void saveStore_Success() {
        // given
        Store store = StoreFixture.createStore();

        // when
        Store saved = storeRepository.save(store);

        // then
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    @DisplayName("성공 - id로 Store 조회")
    void findById_Success() {
        // given
        Store saved = storeRepository.save(StoreFixture.createStore());

        // when
        Optional<Store> found = storeRepository.findById(saved.getId());

        // then
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("성공 - publicId로 Store 조회")
    void findByPublicId_Success() {
        // given
        Store saved = storeRepository.save(StoreFixture.createStore());

        // when
        Optional<Store> found = storeRepository.findByPublicId(saved.getPublicId());

        // then
        assertThat(found).isPresent();
    }
}

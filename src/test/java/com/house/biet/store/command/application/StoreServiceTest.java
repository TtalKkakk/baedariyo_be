package com.house.biet.store.command.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.StoreRepository;
import com.house.biet.store.command.domain.aggregate.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    @Test
    @DisplayName("성공 - Store 저장")
    void save_Success() {
        // given
        Store store = mock(Store.class);
        given(storeRepository.save(store)).willReturn(store);

        // when
        Store result = storeService.save(store);

        // then
        assertThat(result).isSameAs(store);
        then(storeRepository).should().save(store);
    }

    @Test
    @DisplayName("성공 - StoreId로 Store 조회")
    void getStoreById_Success() {
        // given
        Long storeId = 1L;
        Store store = mock(Store.class);

        given(storeRepository.findById(storeId))
                .willReturn(Optional.of(store));

        // when
        Store result = storeService.getStoreById(storeId);

        // then
        assertThat(result).isSameAs(store);
    }

    @Test
    @DisplayName("에러 - 존재하지 않는 StoreId로 Store 조회")
    void getStoreById_Error_NotFoundStore() {
        // given
        Long storeId = 1L;
        given(storeRepository.findById(storeId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> storeService.getStoreById(storeId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.STORE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("성공 - PublicStoreId로 Store 조회")
    void getStoreByPublicId_Success() {
        // given
        UUID publicStoreId = UUID.randomUUID();
        Store store = mock(Store.class);

        given(storeRepository.findByPublicId(publicStoreId))
                .willReturn(Optional.of(store));

        // when
        Store result = storeService.getStoreByPublicId(publicStoreId);

        // then
        assertThat(result).isSameAs(store);
    }

    @Test
    @DisplayName("에러 - 존재하지 않는 PublicStoreId로 Store 조회")
    void getStoreByPublicId_Error_NotFoundStore() {
        // given
        UUID publicStoreId = UUID.randomUUID();
        given(storeRepository.findByPublicId(publicStoreId))
                .willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> storeService.getStoreByPublicId(publicStoreId))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.STORE_NOT_FOUND.getMessage());
    }
}
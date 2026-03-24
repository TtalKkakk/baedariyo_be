package com.house.biet.store.query.application;

import com.house.biet.store.query.StoreQueryRepository;
import com.house.biet.store.query.dto.StoreMenuQueryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StoreQueryServiceTest {

    @InjectMocks
    private StoreQueryServiceImpl storeQueryService;

    @Mock
    private StoreQueryRepository storeQueryRepository;

    @Test
    @DisplayName("성공 - 가게 ID로 메뉴 목록을 조회한다")
    void getMenusByStoreId_Success() {
        // given
        Long storeId = 1L;
        List<StoreMenuQueryDto> menus = List.of(
                new StoreMenuQueryDto(1L, "menu1", 10000, "desc1"),
                new StoreMenuQueryDto(2L, "menu2", 20000, "desc2")
        );
        given(storeQueryRepository.findMenusById(storeId)).willReturn(menus);

        // when
        List<StoreMenuQueryDto> result = storeQueryService.getMenusByStoreId(storeId);

        // then
        assertThat(result).isEqualTo(menus);
    }

    @Test
    @DisplayName("성공 - 가게 Public ID로 메뉴 목록을 조회한다")
    void getMenusByPublicId_Success() {
        // given
        UUID publicStoreId = UUID.randomUUID();
        List<StoreMenuQueryDto> menus = List.of(new StoreMenuQueryDto(1L, "menu1", 10000, "desc1"));
        given(storeQueryRepository.findMenusByPublicId(publicStoreId)).willReturn(menus);

        // when
        List<StoreMenuQueryDto> result = storeQueryService.getMenusByPublicId(publicStoreId);

        // then
        assertThat(result).isEqualTo(menus);
    }
}
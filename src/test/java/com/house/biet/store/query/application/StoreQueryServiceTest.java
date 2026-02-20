package com.house.biet.store.query.application;

import com.house.biet.store.command.domain.entity.Menu;
import com.house.biet.store.query.StoreQueryRepository;
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
    @DisplayName("성공 - StoreId로 Menu 목록 조회")
    void getMenusByStoreId_Success() {
        // given
        Long storeId = 1L;
        List<Menu> menus = List.of(
                org.mockito.Mockito.mock(Menu.class),
                org.mockito.Mockito.mock(Menu.class)
        );

        given(storeQueryRepository.findMenusById(storeId))
                .willReturn(menus);

        // when
        List<Menu> result = storeQueryService.getMenusByStoreId(storeId);

        // then
        assertThat(result).isSameAs(menus);
    }

    @Test
    @DisplayName("성공 - PublicStoreId로 Menu 목록 조회")
    void getMenusByPublicId_Success() {
        // given
        UUID publicStoreId = UUID.randomUUID();
        List<Menu> menus = List.of(
                org.mockito.Mockito.mock(Menu.class)
        );

        given(storeQueryRepository.findMenusByPublicId(publicStoreId))
                .willReturn(menus);

        // when
        List<Menu> result = storeQueryService.getMenusByPublicId(publicStoreId);

        // then
        assertThat(result).isSameAs(menus);
    }
}

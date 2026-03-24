package com.house.biet.store.query;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.fixtures.StoreFixture;
import com.house.biet.store.command.StoreRepository;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.vo.MenuName;
import com.house.biet.store.command.repository.StoreRepositoryJpaAdapter;
import com.house.biet.store.query.dto.StoreMenuQueryDto;
import com.house.biet.store.query.repository.StoreQueryRepositoryJpaAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        StoreQueryRepositoryJpaAdapter.class,
        StoreRepositoryJpaAdapter.class
})
@ActiveProfiles("test")
class StoreQueryRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StoreQueryRepository storeQueryRepository;

    private Store savedStore;

    @BeforeEach
    void setup() {
        Store store = StoreFixture.createStore();
        store.addMenu(new MenuName("menu1"), new Money(10000), "description1");
        store.addMenu(new MenuName("menu2"), new Money(20000), "description2");
        store.addMenu(new MenuName("menu3"), new Money(30000), "description3");
        savedStore = storeRepository.save(store);
    }

    @Test
    @DisplayName("finds menu query dtos by store id")
    void findMenusById_Success() {
        List<StoreMenuQueryDto> menus = storeQueryRepository.findMenusById(savedStore.getId());

        assertThat(menus).hasSize(3);
        assertThat(menus).extracting(StoreMenuQueryDto::menuName)
                .containsExactlyInAnyOrder("menu1", "menu2", "menu3");
    }

    @Test
    @DisplayName("returns empty when store id does not exist")
    void findMenusById_Empty() {
        List<StoreMenuQueryDto> menus = storeQueryRepository.findMenusById(-1L);

        assertThat(menus).isEmpty();
    }

    @Test
    @DisplayName("finds menu query dtos by public store id")
    void findMenusByPublicId_Success() {
        List<StoreMenuQueryDto> menus = storeQueryRepository.findMenusByPublicId(savedStore.getPublicId());

        assertThat(menus).hasSize(3);
        assertThat(menus).extracting(StoreMenuQueryDto::price)
                .containsExactlyInAnyOrder(10000, 20000, 30000);
    }

    @Test
    @DisplayName("returns empty when public store id is null")
    void findMenusByPublicId_Empty() {
        List<StoreMenuQueryDto> menus = storeQueryRepository.findMenusByPublicId(null);

        assertThat(menus).isEmpty();
    }
}

package com.house.biet.store.query;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.fixtures.StoreFixture;
import com.house.biet.store.command.StoreRepository;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.entity.Menu;
import com.house.biet.store.command.domain.vo.MenuName;
import com.house.biet.store.command.repository.StoreRepositoryJpaAdapter;
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
        StoreRepositoryJpaAdapter.class}
)
@ActiveProfiles("test")
class StoreQueryRepositoryTest {

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    StoreQueryRepository storeQueryRepository;

    Store savedStore;

    @BeforeEach
    void setup() {
        Store store = StoreFixture.createStore();
        store.addMenu(new MenuName("menu1"), new Money(10000), "description1");
        store.addMenu(new MenuName("menu2"), new Money(20000), "description2");
        store.addMenu(new MenuName("menu3"), new Money(30000), "description3");

        savedStore = storeRepository.save(store);
    }

    @Test
    @DisplayName("성공 - storeId로 메뉴 조회")
    void findMenusById_Success() {
        // when
        List<Menu> menus = storeQueryRepository.findMenusById(savedStore.getId());

        // then
        assertThat(menus).hasSize(3);
    }

    @Test
    @DisplayName("성공 - 없는 storeId로 조회 시 빈 리스트")
    void findMenusById_Empty() {
        // when
        List<Menu> menus = storeQueryRepository.findMenusById(-1L);

        // then
        assertThat(menus).isEmpty();
    }

    @Test
    @DisplayName("성공 - publicStoreId로 메뉴 조회")
    void findMenusByPublicId_Success() {
        // when
        List<Menu> menus = storeQueryRepository.findMenusByPublicId(savedStore.getPublicId());

        // then
        assertThat(menus).hasSize(3);
    }

    @Test
    @DisplayName("성공 - 없는 publicStoreId로 조회 시 빈 리스트")
    void findMenusByPublicId_Empty() {
        // when
        List<Menu> menus = storeQueryRepository.findMenusByPublicId(null);

        // then
        assertThat(menus).isEmpty();
    }
}

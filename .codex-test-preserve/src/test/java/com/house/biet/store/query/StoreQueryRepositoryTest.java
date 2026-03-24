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
    @DisplayName("성공 - 가게 ID로 메뉴 목록을 조회한다")
    void findMenusById_Success() {
        // when
        List<StoreMenuQueryDto> menus = storeQueryRepository.findMenusById(savedStore.getId());

        // then
        assertThat(menus).hasSize(3);
        assertThat(menus).extracting(StoreMenuQueryDto::menuName).containsExactlyInAnyOrder("menu1", "menu2", "menu3");
    }

    @Test
    @DisplayName("성공 - 존재하지 않는 가게 ID를 조회하면 빈 목록을 반환한다")
    void findMenusById_Success_WhenStoreDoesNotExist() {
        // when
        List<StoreMenuQueryDto> menus = storeQueryRepository.findMenusById(-1L);

        // then
        assertThat(menus).isEmpty();
    }

    @Test
    @DisplayName("성공 - 가게 Public ID로 메뉴 목록을 조회한다")
    void findMenusByPublicId_Success() {
        // when
        List<StoreMenuQueryDto> menus = storeQueryRepository.findMenusByPublicId(savedStore.getPublicId());

        // then
        assertThat(menus).hasSize(3);
        assertThat(menus).extracting(StoreMenuQueryDto::price).containsExactlyInAnyOrder(10000, 20000, 30000);
    }

    @Test
    @DisplayName("성공 - null Public ID를 조회하면 빈 목록을 반환한다")
    void findMenusByPublicId_Success_WhenPublicIdIsNull() {
        // when
        List<StoreMenuQueryDto> menus = storeQueryRepository.findMenusByPublicId(null);

        // then
        assertThat(menus).isEmpty();
    }
}
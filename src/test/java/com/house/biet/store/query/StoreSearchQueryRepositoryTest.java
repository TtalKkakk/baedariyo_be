package com.house.biet.store.query;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.common.domain.vo.Address;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.vo.*;
import com.house.biet.store.query.dto.StoreSearchQueryDto;
import com.house.biet.store.query.repository.StoreSearchQueryRepositoryImpl;
import com.house.biet.support.config.QueryDslTestConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        StoreSearchQueryRepositoryImpl.class,
        QueryDslTestConfig.class
})
class StoreSearchQueryRepositoryTest {

    @Autowired
    private StoreSearchQueryRepository repository;

    @Autowired
    private EntityManager em;

    private Store createStore(
            String name,
            StoreCategory category,
            double lat,
            double lng,
            int minOrder
    ) {

        Store store = Store.create(
                new StoreName(name),
                category,
                new Address("도로명", "지번", "상세"),
                new GeoLocation(lat, lng),
                new StoreThumbnail("url"),
                null,
                null,
                new Money(minOrder),
                new Money(3000)
        );

        em.persist(store);
        return store;
    }

    @Test
    @DisplayName("성공 - 키워드로 가게 검색")
    void searchStores_Success_keyword() {

        // given
        createStore("맛있는 치킨집", StoreCategory.CHICKEN, 37.5, 127.0, 15000);
        createStore("피자천국", StoreCategory.PIZZA, 37.5, 127.1, 15000);

        em.flush();
        em.clear();

        // when
        List<StoreSearchQueryDto> result =
                repository.searchStores(
                        "치킨",
                        null,
                        37.5,
                        127.0,
                        0,
                        10
                );

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).storeName()).isEqualTo("맛있는 치킨집");
    }

    @Test
    @DisplayName("성공 - 카테고리로 가게 검색")
    void searchStores_Success_category() {

        // given
        createStore("치킨집", StoreCategory.CHICKEN, 37.5, 127.0, 15000);
        createStore("피자집", StoreCategory.PIZZA, 37.5, 127.0, 15000);

        em.flush();
        em.clear();

        // when
        List<StoreSearchQueryDto> result =
                repository.searchStores(
                        null,
                        StoreCategory.CHICKEN,
                        37.5,
                        127.0,
                        0,
                        10
                );

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).storeName()).isEqualTo("치킨집");
    }

    @Test
    @DisplayName("성공 - 거리 기준 가게 정렬")
    void searchStores_Success_distanceSort() {

        // given
        createStore("가까운 가게", StoreCategory.CHICKEN, 37.5001, 127.0001, 15000);
        createStore("먼 가게", StoreCategory.CHICKEN, 37.6, 127.1, 15000);

        em.flush();
        em.clear();

        // when
        List<StoreSearchQueryDto> result =
                repository.searchStores(
                        null,
                        StoreCategory.CHICKEN,
                        37.5,
                        127.0,
                        0,
                        10
                );

        // then
        assertThat(result.get(0).storeName()).isEqualTo("가까운 가게");
    }

    @Test
    @DisplayName("성공 - 페이징 적용된 가게 검색")
    void searchStores_Success_paging() {

        // given
        for (int i = 0; i < 20; i++) {
            createStore("가게" + i, StoreCategory.CHICKEN, 37.5 + i * 0.001, 127.0, 15000);
        }

        em.flush();
        em.clear();

        // when
        List<StoreSearchQueryDto> result =
                repository.searchStores(
                        null,
                        StoreCategory.CHICKEN,
                        37.5,
                        127.0,
                        0,
                        10
                );

        // then
        assertThat(result).hasSize(10);
    }
}
package com.house.biet.store.command.application;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.common.domain.vo.Address;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.fixtures.BusinessHoursFixture;
import com.house.biet.fixtures.StoreOperationInfoFixture;
import com.house.biet.global.geocoding.application.GeocodingService;
import com.house.biet.global.geocoding.dto.GeoPoint;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.application.dto.StoreCreateRequestDto;
import com.house.biet.store.command.application.dto.StoreCreateResponseDto;
import com.house.biet.store.command.domain.vo.GeoLocation;
import com.house.biet.store.query.dto.StoreDetailWithMenuAndReviewResponseDto;
import com.house.biet.support.config.ServiceIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class StoreFacadeIntegrationTest extends ServiceIntegrationTest {

    @Autowired
    private StoreFacade storeFacade;

    @MockitoBean
    private GeocodingService geocodingService;

    // 공용 변수
    private String storeName;
    private StoreCategory storeCategory;
    private Address storeAddress;
    private String thumbnailUrl;
    private Money minimumOrderAmount;
    private Money deliveryFee;

    private StoreCreateRequestDto request;

    @BeforeEach
    void setUp() {
        given(geocodingService.geocode(any()))
                .willReturn(new GeoPoint(37.0, 127.0));

        storeName = "통합테스트가게";
        storeCategory = StoreCategory.CHICKEN;
        storeAddress = new Address("roadAddress", "jibunAddress", "detailAddress");
        thumbnailUrl = "http://image.com";
        minimumOrderAmount = new Money(15000);
        deliveryFee = new Money(3000);

        request = new StoreCreateRequestDto(
                storeName,
                storeCategory,
                storeAddress,
                thumbnailUrl,
                BusinessHoursFixture.withDefaultWeekdays(),
                StoreOperationInfoFixture.aStoreOperationInfo().build(),
                minimumOrderAmount,
                deliveryFee
        );
    }

    @Test
    @DisplayName("성공 - 가게 생성")
    void createStore_Success() {
        // when
        StoreCreateResponseDto response = storeFacade.createStore(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.storeName()).isEqualTo(storeName);
    }

    @Test
    @DisplayName("성공 - 가게 상세 조회")
    void getStoreDetail_Success() {
        // given
        StoreCreateResponseDto created = storeFacade.createStore(request);

        // when
        StoreDetailWithMenuAndReviewResponseDto detail =
                storeFacade.getStoreDetail(created.storePublicId());

        // then
        assertThat(detail).isNotNull();
        assertThat(detail.storeName()).isEqualTo(storeName);
        assertThat(detail.menus()).isEmpty();
        assertThat(detail.recentPhotoReviews()).isEmpty();
    }

    @Test
    @DisplayName("실패 - 가게 미존재")
    void getStoreDetail_Error_StoreNotFound() {
        // given
        UUID randomId = UUID.randomUUID();

        // when & then
        assertThatThrownBy(() -> storeFacade.getStoreDetail(randomId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(ErrorCode.STORE_NOT_FOUND.getMessage());
    }
}
package com.house.biet.store.command.application;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.common.domain.vo.Address;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.fixtures.BusinessHoursFixture;
import com.house.biet.fixtures.StoreOperationInfoFixture;
import com.house.biet.global.geocoding.application.GeocodingService;
import com.house.biet.global.geocoding.dto.GeoPoint;
import com.house.biet.store.command.application.dto.StoreCreateRequestDto;
import com.house.biet.store.command.application.dto.StoreCreateResponseDto;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.vo.BusinessHours;
import com.house.biet.store.command.domain.vo.GeoLocation;
import com.house.biet.store.command.domain.vo.StoreName;
import com.house.biet.store.command.domain.vo.StoreOperationInfo;
import com.house.biet.store.command.domain.vo.StoreThumbnail;
import com.house.biet.store.query.application.StoreQueryService;
import com.house.biet.store.query.application.StoreReviewQueryService;
import com.house.biet.store.query.dto.StoreDetailWithMenuAndReviewResponseDto;
import com.house.biet.store.query.dto.StoreMenuQueryDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class StoreFacadeTest {

    @Mock
    private StoreService storeService;

    @Mock
    private StoreQueryService storeQueryService;

    @Mock
    private GeocodingService geocodingService;

    @Mock
    private StoreReviewQueryService storeReviewQueryService;

    @InjectMocks
    private StoreFacade storeFacade;

    private String storeName;
    private StoreCategory storeCategory;
    private Address storeAddress;
    private GeoLocation storeGeoLocation;
    private String thumbnailUrl;
    private BusinessHours businessHours;
    private StoreOperationInfo operationInfo;
    private Money minimumOrderAmount;
    private Money deliveryFee;
    private StoreCreateRequestDto request;
    private Store store;

    @BeforeEach
    void setUp() {
        storeName = "test store";
        storeCategory = StoreCategory.CHICKEN;
        storeAddress = new Address("roadAddress", "jibunAddress", "detailAddress");
        storeGeoLocation = new GeoLocation(37.2123, 129.222);
        thumbnailUrl = "http://image.com";
        businessHours = BusinessHoursFixture.withDefaultWeekdays();
        operationInfo = StoreOperationInfoFixture.aStoreOperationInfo().build();
        minimumOrderAmount = new Money(15000);
        deliveryFee = new Money(3000);

        request = new StoreCreateRequestDto(
                storeName,
                storeCategory,
                storeAddress,
                thumbnailUrl,
                businessHours,
                operationInfo,
                minimumOrderAmount,
                deliveryFee
        );

        store = Store.create(
                new StoreName(storeName),
                storeCategory,
                storeAddress,
                storeGeoLocation,
                new StoreThumbnail(thumbnailUrl),
                businessHours,
                operationInfo,
                minimumOrderAmount,
                deliveryFee
        );
    }

    @Test
    @DisplayName("creates store")
    void createStore_Success() {
        given(storeService.save(any(Store.class))).willReturn(store);
        given(geocodingService.geocode(any())).willReturn(new GeoPoint(37.0, 127.0));

        StoreCreateResponseDto response = storeFacade.createStore(request);

        assertThat(response).isNotNull();
        verify(storeService).save(any(Store.class));
    }

    @Test
    @DisplayName("maps store create request")
    void createStoreMapping_Success() {
        ArgumentCaptor<Store> captor = ArgumentCaptor.forClass(Store.class);
        given(storeService.save(any(Store.class))).willReturn(store);
        given(geocodingService.geocode(any())).willReturn(new GeoPoint(37.0, 127.0));

        storeFacade.createStore(request);

        verify(storeService).save(captor.capture());
        Store captured = captor.getValue();

        assertThat(captured.getStoreName().getValue()).isEqualTo(storeName);
        assertThat(captured.getStoreCategory()).isEqualTo(storeCategory);
        assertThat(captured.getThumbnail().getImageUrl()).isEqualTo(thumbnailUrl);
        assertThat(captured.getMinimumOrderAmount()).isEqualTo(minimumOrderAmount);
        assertThat(captured.getDeliveryFee()).isEqualTo(deliveryFee);
    }

    @Test
    @DisplayName("gets store detail with query dto menus")
    void getStoreDetail_Success() {
        UUID storeId = UUID.randomUUID();

        given(storeService.getStoreByPublicId(storeId)).willReturn(store);
        given(storeQueryService.getMenusByPublicId(storeId)).willReturn(
                List.of(new StoreMenuQueryDto(1L, "menu1", 10000, "desc1"))
        );
        given(storeReviewQueryService.findTop3PhotoReviewsByStore(storeId)).willReturn(List.of());

        StoreDetailWithMenuAndReviewResponseDto response = storeFacade.getStoreDetail(storeId);

        assertThat(response).isNotNull();
        assertThat(response.storePublicId()).isEqualTo(store.getPublicId());
        assertThat(response.storeName()).isEqualTo(storeName);
        assertThat(response.menus()).hasSize(1);
        assertThat(response.menus().get(0).menuName()).isEqualTo("menu1");
        assertThat(response.menus().get(0).price()).isEqualTo(10000);
        assertThat(response.recentPhotoReviews()).isEmpty();
    }

    @Test
    @DisplayName("throws when store is not found")
    void getStoreDetail_Error_StoreNotFound() {
        UUID storeId = UUID.randomUUID();
        given(storeService.getStoreByPublicId(storeId)).willThrow(new RuntimeException("STORE_NOT_FOUND"));

        assertThatThrownBy(() -> storeFacade.getStoreDetail(storeId))
                .isInstanceOf(RuntimeException.class);
    }
}

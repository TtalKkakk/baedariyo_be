package com.house.biet.store.command.application;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.fixtures.BusinessHoursFixture;
import com.house.biet.fixtures.StoreOperationInfoFixture;
import com.house.biet.store.command.application.dto.StoreCreateRequestDto;
import com.house.biet.store.command.application.dto.StoreCreateResponseDto;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.vo.BusinessHours;
import com.house.biet.store.command.domain.vo.StoreName;
import com.house.biet.store.command.domain.vo.StoreOperationInfo;
import com.house.biet.store.command.domain.vo.StoreThumbnail;
import com.house.biet.store.query.application.StoreQueryService;
import com.house.biet.store.query.application.StoreReviewQueryService;
import com.house.biet.store.query.dto.StoreDetailWithMenuAndReviewResponseDto;
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
    private StoreReviewQueryService storeReviewQueryService;

    @InjectMocks
    private StoreFacade storeFacade;

    private String storeName;
    private StoreCategory storeCategory;
    private String thumbnailUrl;
    private BusinessHours businessHours;
    private StoreOperationInfo operationInfo;
    private Money minimumOrderAmount;
    private Money deliveryFee;

    private StoreCreateRequestDto request;
    private Store store;

    @BeforeEach
    void setUp() {
        storeName = "테스트가게";
        storeCategory = StoreCategory.CHICKEN;
        thumbnailUrl = "http://image.com";
        businessHours = BusinessHoursFixture.withDefaultWeekdays();
        operationInfo = StoreOperationInfoFixture.aStoreOperationInfo().build();
        minimumOrderAmount = new Money(15000);
        deliveryFee = new Money(3000);

        request = new StoreCreateRequestDto(
                storeName,
                storeCategory,
                thumbnailUrl,
                businessHours,
                operationInfo,
                minimumOrderAmount,
                deliveryFee
        );

        store = Store.create(
                new StoreName(storeName),
                storeCategory,
                new StoreThumbnail(thumbnailUrl),
                businessHours,
                operationInfo,
                minimumOrderAmount,
                deliveryFee
        );
    }

    @Test
    @DisplayName("성공 - 가게 생성")
    void createStore_Success() {
        given(storeService.save(any(Store.class))).willReturn(store);

        StoreCreateResponseDto response = storeFacade.createStore(request);

        assertThat(response).isNotNull();
        verify(storeService).save(any(Store.class));
    }

    @Test
    @DisplayName("성공 - 가게 생성 시 요청값 매핑")
    void createStoreMapping_Success() {
        ArgumentCaptor<Store> captor = ArgumentCaptor.forClass(Store.class);
        given(storeService.save(any(Store.class))).willReturn(store);

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
    @DisplayName("성공 - 가게 상세 조회")
    void getStoreDetail_Success() {
        UUID storeId = UUID.randomUUID();

        given(storeService.getStoreByPublicId(storeId)).willReturn(store);
        given(storeQueryService.getMenusByPublicId(storeId)).willReturn(List.of());
        given(storeReviewQueryService.findTop3PhotoReviewsByStore(storeId))
                .willReturn(List.of());

        StoreDetailWithMenuAndReviewResponseDto response =
                storeFacade.getStoreDetail(storeId);

        assertThat(response).isNotNull();
        assertThat(response.storePublicId()).isEqualTo(store.getPublicId());
        assertThat(response.storeName()).isEqualTo(storeName);
        assertThat(response.menus()).isEmpty();
        assertThat(response.recentPhotoReviews()).isEmpty();

        verify(storeService).getStoreByPublicId(storeId);
        verify(storeQueryService).getMenusByPublicId(storeId);
        verify(storeReviewQueryService)
                .findTop3PhotoReviewsByStore(storeId);
    }

    @Test
    @DisplayName("실패 - 가게 미존재")
    void getStoreDetail_Error_StoreNotFound() {
        UUID storeId = UUID.randomUUID();

        given(storeService.getStoreByPublicId(storeId))
                .willThrow(new RuntimeException("STORE_NOT_FOUND"));

        assertThatThrownBy(() -> storeFacade.getStoreDetail(storeId))
                .isInstanceOf(RuntimeException.class);
    }
}
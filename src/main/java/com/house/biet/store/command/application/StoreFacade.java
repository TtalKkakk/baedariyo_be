package com.house.biet.store.command.application;

import com.house.biet.common.domain.vo.Address;
import com.house.biet.global.geocoding.application.GeocodingService;
import com.house.biet.global.geocoding.dto.GeoPoint;
import com.house.biet.store.command.application.dto.StoreCreateRequestDto;
import com.house.biet.store.command.application.dto.StoreCreateResponseDto;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.vo.GeoLocation;
import com.house.biet.store.command.domain.vo.StoreName;
import com.house.biet.store.command.domain.vo.StoreThumbnail;
import com.house.biet.store.query.application.StoreQueryService;
import com.house.biet.store.query.application.StoreReviewQueryService;
import com.house.biet.store.query.dto.StoreDetailWithMenuAndReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreFacade {

    private final GeocodingService geocodingService;
    private final StoreService storeService;
    private final StoreQueryService storeQueryService;
    private final StoreReviewQueryService storeReviewQueryService;

    /**
     * 가게 생성
     *
     * @param requestDto 요청 정보
     * @return createStore 결과
     */
    @Transactional
    public StoreCreateResponseDto createStore(StoreCreateRequestDto requestDto) {
        // TODO: accountId를 이용해서 연동할 것!

        Address storeAddress = requestDto.storeAddress();
        GeoPoint geoPoint = geocodingService.geocode(storeAddress.getDetailAddress());
        GeoLocation geoLocation = new GeoLocation(geoPoint.latitude(), geoPoint.longitude());

        Store store = Store.create(
                new StoreName(requestDto.storeName()),
                requestDto.storeCategory(),
                storeAddress,
                geoLocation,
                requestDto.thumbnailUrl() != null ? new StoreThumbnail(requestDto.thumbnailUrl()) : null,
                requestDto.businessHours(),
                requestDto.operationInfo(),
                requestDto.minimumOrderAmount(),
                requestDto.deliveryFee()
        );

        return StoreCreateResponseDto.fromEntity(storeService.save(store));
    }

    /**
     * 가게 상세 조회 (메뉴 + 최근 사진 리뷰 3개 포함)
     *
     * @param storePublicId storePublicId 값
     * @return getStoreDetail 결과
     */
    @Transactional(readOnly = true)
    public StoreDetailWithMenuAndReviewResponseDto getStoreDetail(UUID storePublicId) {
        // 1. Store 조회
        Store store = storeService.getStoreByPublicId(storePublicId);

        // 2. 메뉴 조회
        List<StoreDetailWithMenuAndReviewResponseDto.MenuDto> menus = storeQueryService.getMenusByPublicId(storePublicId)
                .stream()
                .map(StoreDetailWithMenuAndReviewResponseDto.MenuDto::from)
                .collect(Collectors.toList());

        // 3. 최근 사진 리뷰 3개 조회
        List<StoreDetailWithMenuAndReviewResponseDto.StoreReviewPhotoDto> recentPhotoReviews =
                storeReviewQueryService.findTop3PhotoReviewsByStore(storePublicId)
                        .stream()
                        .map(StoreDetailWithMenuAndReviewResponseDto.StoreReviewPhotoDto::fromDtoEntity)
                        .collect(Collectors.toList());

        // 4. DTO 조립
        return new StoreDetailWithMenuAndReviewResponseDto(
                store.getPublicId(),
                store.getStoreName().getValue(),
                store.getThumbnail().getImageUrl(),
                store.getRating().getTotalRating(),
                store.getRating().getReviewCount(),
                menus,
                recentPhotoReviews
        );
    }
}

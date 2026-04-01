package com.house.biet.api;

import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.store.command.application.StoreFacade;
import com.house.biet.store.command.application.dto.StoreCreateRequestDto;
import com.house.biet.store.command.application.dto.StoreCreateResponseDto;
import com.house.biet.store.query.application.StoreQueryService;
import com.house.biet.store.query.application.StoreSearchQueryService;
import com.house.biet.store.query.dto.StoreDetailWithMenuAndReviewResponseDto;
import com.house.biet.store.query.dto.StoreMenuQueryDto;
import com.house.biet.store.query.dto.StoreSearchRequestDto;
import com.house.biet.store.query.dto.StoreSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreSearchQueryService storeSearchQueryService;
    private final StoreQueryService storeQueryService;
    private final StoreFacade storeFacade;

    // ----------------------
    // Store 검색
    // ----------------------
    /**
     * Stores을 검색한다
     *
     * @param principal 인증 사용자 정보
     * @param requestDto 요청 정보
     * @return searchStores 결과
     */
    @GetMapping
    public ResponseEntity<CustomApiResponse<List<StoreSearchResponseDto>>> searchStores(
            @AuthenticationPrincipal AuthPrincipal principal,
            @ModelAttribute StoreSearchRequestDto requestDto
    ) {
        List<StoreSearchResponseDto> responseDto = storeSearchQueryService.searchStores(
                principal.accountId(),
                requestDto.keyword(),
                requestDto.storeCategory(),
                requestDto.latitude(),
                requestDto.longitude(),
                requestDto.page(),
                requestDto.size()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.STORE_GET_LIST_SUCCESS, responseDto)
        );
    }

    // ----------------------
    // Store CRUD
    // ----------------------
    /**
     * 가게을 생성한다
     *
     * @param requestDto 요청 정보
     * @return createStore 결과
     */
    @PostMapping
    public ResponseEntity<CustomApiResponse<StoreCreateResponseDto>> createStore(
            @RequestBody StoreCreateRequestDto requestDto)
    {
        StoreCreateResponseDto responseDto = storeFacade.createStore(requestDto);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.STORE_CREATE_SUCCESS, responseDto)
        );
    }

    /**
     * 가게 가게 공개 식별자을 조회한다
     *
     * @param storePublicId storePublicId 값
     * @return getStoreByStorePublicId 결과
     */
    @GetMapping("/{storePublicId}")
    public ResponseEntity<CustomApiResponse<StoreDetailWithMenuAndReviewResponseDto>> getStoreByStorePublicId(
            @PathVariable UUID storePublicId
    ) {
        StoreDetailWithMenuAndReviewResponseDto responseDto =
                storeFacade.getStoreDetail(storePublicId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.OK, responseDto)
        );
    }

    // ----------------------
    // Store Menu 조회
    // ----------------------

    /**
     * Menus 가게 공개 식별자을 조회한다
     *
     * @param storePublicId storePublicId 값
     * @return getMenusByStorePublicId 결과
     */
    @GetMapping("/{storePublicId}/menus")
    public ResponseEntity<CustomApiResponse<List<StoreMenuQueryDto>>> getMenusByStorePublicId(
            @PathVariable UUID storePublicId
    ) {
        List<StoreMenuQueryDto> menus = storeQueryService.getMenusByPublicId(storePublicId);
        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.OK, menus)
        );
    }
}

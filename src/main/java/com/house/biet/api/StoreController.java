package com.house.biet.api;

import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.store.command.application.StoreFacade;
import com.house.biet.store.command.application.StoreService;
import com.house.biet.store.command.application.dto.StoreCreateRequestDto;
import com.house.biet.store.command.application.dto.StoreCreateResponseDto;
import com.house.biet.store.query.application.StoreQueryService;
import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.entity.Menu;
import com.house.biet.store.query.application.StoreSearchQueryService;
import com.house.biet.store.query.dto.StoreDetailWithMenuAndReviewResponseDto;
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
    @PostMapping
    public ResponseEntity<CustomApiResponse<StoreCreateResponseDto>> createStore(
            @RequestBody StoreCreateRequestDto requestDto)
    {
        StoreCreateResponseDto responseDto = storeFacade.createStore(requestDto);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.STORE_CREATE_SUCCESS, responseDto)
        );
    }

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

    @GetMapping("/{storePublicId}/menus")
    public ResponseEntity<CustomApiResponse<List<Menu>>> getMenusByStorePublicId(
            @PathVariable UUID storePublicId
    ) {
        List<Menu> menus = storeQueryService.getMenusByPublicId(storePublicId);
        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.OK, menus)
        );
    }
}
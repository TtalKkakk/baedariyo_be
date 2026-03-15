package com.house.biet.api;

import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.storeSearch.query.dto.KeywordListResponseDto;
import com.house.biet.storeSearch.query.popular.application.PopularSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search/popular")
public class PopularSearchController {

    private final PopularSearchService popularSearchService;

    @GetMapping
    public ResponseEntity<CustomApiResponse<KeywordListResponseDto>> getPopularKeywords() {

        KeywordListResponseDto responseDto = new KeywordListResponseDto(popularSearchService.getTopKeywords());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.SEARCH_POPULAR_SUCCESS, responseDto)
        );
    }
}

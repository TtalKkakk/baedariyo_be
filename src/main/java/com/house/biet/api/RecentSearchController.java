package com.house.biet.api;

import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.storeSearch.query.dto.KeywordListResponseDto;
import com.house.biet.storeSearch.query.recent.application.RecentSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search/recent")
public class RecentSearchController {

    private final RecentSearchService recentSearchService;

    @GetMapping
    public ResponseEntity<CustomApiResponse<KeywordListResponseDto>> getRecentKeywords(
            @RequestParam Long userId
    ) {

        KeywordListResponseDto responseDto = new KeywordListResponseDto(recentSearchService.getRecentKeywords(userId));

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.SEARCH_RECENT_SUCCESS, responseDto)
        );
    }

    @DeleteMapping("/{keyword}")
    public ResponseEntity<CustomApiResponse<Void>> deleteKeyword(
            @RequestParam Long userId,
            @PathVariable String keyword
    ) {

        recentSearchService.deleteKeyword(userId, keyword);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.SEARCH_RECENT_DELETE_SUCCESS)
        );
    }

    @DeleteMapping
    public ResponseEntity<CustomApiResponse<Void>> deleteAll(
            @RequestParam Long userId
    ) {

        recentSearchService.deleteAll(userId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.SEARCH_RECENT_DELETE_ALL_SUCCESS)
        );
    }
}

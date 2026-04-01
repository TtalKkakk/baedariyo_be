package com.house.biet.api;

import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.storeSearch.query.autocomplete.application.AutoCompleteSearchService;
import com.house.biet.storeSearch.query.dto.KeywordListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search/autocomplete")
public class AutoCompleteController {

    private final AutoCompleteSearchService autoCompleteSearchService;

    /**
     * 대상을 검색한다
     *
     * @param keyword 검색어
     * @return search 결과
     */
    @GetMapping
    public ResponseEntity<CustomApiResponse<KeywordListResponseDto>> search(
            @RequestParam String keyword
    ) {

        KeywordListResponseDto responseDto = new KeywordListResponseDto(autoCompleteSearchService.search(keyword));

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.SEARCH_AUTOCOMPLETE_SUCCESS, responseDto)
        );
    }
}

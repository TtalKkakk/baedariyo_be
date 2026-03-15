package com.house.biet.storeSearch.query.common;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class SearchKeywordNormalizer {

    public String normalize(String keyword) {
        if (keyword == null)
            return "";

        return keyword
                .trim()
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9가-힣 ]", "")
                .replaceAll("\\s+", " ");
    }
}

package com.house.biet.storeSearch.query.config;

import java.time.LocalDate;

public class StoreSearchRedisKey {

    private static final String AUTOCOMPLETE_STORE = "autocomplete:store";
    private static final String RECENT_SEARCH_USER = "recent:search:user:";
    private static final String POPULAR_SEARCH = "popular:search:";

    public static String autoCompleteSearchKey() {
        return AUTOCOMPLETE_STORE;
    }

    public static String recentSearchKey(Long userId) {
        return RECENT_SEARCH_USER + userId;
    }

    public static String popularSearchKey() {
        return POPULAR_SEARCH + LocalDate.now();
    }
}

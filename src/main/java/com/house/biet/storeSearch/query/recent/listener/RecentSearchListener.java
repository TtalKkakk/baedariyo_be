package com.house.biet.storeSearch.query.recent.listener;

import com.house.biet.storeSearch.query.event.dto.StoreSearchEvent;
import com.house.biet.storeSearch.query.recent.application.RecentSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecentSearchListener {

    private final RecentSearchService recentSearchService;

    @EventListener
    public void handle(StoreSearchEvent event) {
        recentSearchService.save(
                event.userId(),
                event.keyword()
        );
    }
}

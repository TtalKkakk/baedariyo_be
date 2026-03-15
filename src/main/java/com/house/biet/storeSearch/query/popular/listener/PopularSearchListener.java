package com.house.biet.storeSearch.query.popular.listener;

import com.house.biet.storeSearch.query.event.dto.StoreSearchEvent;
import com.house.biet.storeSearch.query.popular.application.PopularSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PopularSearchListener {

    private final PopularSearchService popularSearchService;

    @Async
    @EventListener
    public void handle(StoreSearchEvent event) {

        popularSearchService.increase(
                event.keyword()
        );
    }
}

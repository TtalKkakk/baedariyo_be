package com.house.biet.storeSearch.query.autocomplete.event;

import com.house.biet.storeSearch.query.autocomplete.application.AutoCompleteSearchService;
import com.house.biet.storeSearch.query.event.dto.StoreSearchEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoCompleteListener {

    private final AutoCompleteSearchService autoCompleteSearchService;

    @Async
    @EventListener
    public void handleStoreSearchEvent(StoreSearchEvent event) {

        if (event.resultCount() == 0)
            return;

        autoCompleteSearchService.registerKeyword(
                event.keyword()
        );
    }
}

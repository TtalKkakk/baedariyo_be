package com.house.biet.storeSearch.query.autocomplete.port;

import java.util.List;

public interface AutoCompleteSearchRepositoryPort {

    void saveKeyword(String keyword);

    List<String> search(String prefix);
}

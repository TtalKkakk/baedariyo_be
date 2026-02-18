package com.house.biet.store.query.application;

import com.house.biet.store.command.domain.entity.Menu;
import com.house.biet.store.query.StoreQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreQueryServiceImpl implements StoreQueryService {

    private final StoreQueryRepository storeQueryRepository;

    @Override
    public List<Menu> getMenusByStoreId(Long storeId) {
        return storeQueryRepository.findMenusById(storeId);
    }

    @Override
    public List<Menu> getMenusByPublicId(UUID publicStoreId) {
        return storeQueryRepository.findMenusByPublicId(publicStoreId);
    }
}

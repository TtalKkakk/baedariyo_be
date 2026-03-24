package com.house.biet.store.query.application;

import com.house.biet.store.query.StoreQueryRepository;
import com.house.biet.store.query.dto.StoreMenuQueryDto;
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
    public List<StoreMenuQueryDto> getMenusByStoreId(Long storeId) {
        return storeQueryRepository.findMenusById(storeId);
    }

    @Override
    public List<StoreMenuQueryDto> getMenusByPublicId(UUID publicStoreId) {
        return storeQueryRepository.findMenusByPublicId(publicStoreId);
    }
}

package com.house.biet.store.query.repository;

import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.query.dto.StoreMenuQueryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface StoreQueryRepositoryJpa extends JpaRepository<Store, Long> {

    @Query("""
            select new com.house.biet.store.query.dto.StoreMenuQueryDto(
                m.id,
                m.menuName.value,
                m.price.amount,
                m.menuDescription
            )
            from Menu m
            where m.store.id = :storeId
            """)
    List<StoreMenuQueryDto> findMenusById(Long storeId);

    @Query("""
            select new com.house.biet.store.query.dto.StoreMenuQueryDto(
                m.id,
                m.menuName.value,
                m.price.amount,
                m.menuDescription
            )
            from Menu m
            where m.store.publicId = :publicStoreId
            """)
    List<StoreMenuQueryDto> findMenusByPublicId(UUID publicStoreId);
}

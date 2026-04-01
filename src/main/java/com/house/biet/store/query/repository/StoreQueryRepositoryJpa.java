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
    /**
     * 가게 메뉴 목록을 조회한다
     *
     * @param storeId 가게 식별자
     * @return 조회 결과 목록
     */
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
    /**
     * 가게 메뉴 목록을 조회한다
     *
     * @param publicStoreId 가게 공개 식별자
     * @return 조회 결과 목록
     */
    List<StoreMenuQueryDto> findMenusByPublicId(UUID publicStoreId);
}

package com.house.biet.store.query.repository;

import com.house.biet.store.command.domain.aggregate.Store;
import com.house.biet.store.command.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface StoreQueryRepositoryJpa
        extends JpaRepository<Store, Long> {

    @Query("select m from Menu m where m.store.id = :storeId")
    List<Menu> findMenusById(Long storeId);

    @Query("select m from Menu m where m.store.publicId = :publicStoreId")
    List<Menu> findMenusByPublicId(UUID publicStoreId);
}

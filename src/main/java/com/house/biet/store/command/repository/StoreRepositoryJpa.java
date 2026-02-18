package com.house.biet.store.command.repository;

import com.house.biet.store.command.domain.aggregate.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface StoreRepositoryJpa
        extends JpaRepository<Store, Long> {

    @Query("select s from Store s where s.publicId = :publicStoreId")
    Optional<Store> findByPublicId(UUID publicStoreId);
}

package com.house.biet.rider.command.infrastructure;

import com.house.biet.rider.command.domain.entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RiderRepositoryJpa
        extends JpaRepository<Rider, Long> {
}

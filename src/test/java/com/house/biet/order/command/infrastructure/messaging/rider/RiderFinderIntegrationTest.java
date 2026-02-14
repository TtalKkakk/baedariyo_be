package com.house.biet.order.command.infrastructure.messaging.rider;

import com.house.biet.order.command.application.port.RiderCandidate;
import com.house.biet.order.command.application.port.RiderFinder;
import com.house.biet.order.command.domain.event.OrderCreatedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("test")
class RiderFinderIntegrationTest {

    @Container
    static GenericContainer<?> redis =
            new GenericContainer<>("redis:7.2-alpine")
                    .withExposedPorts(6379);

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port",
                () -> redis.getMappedPort(6379));
    }

    @Autowired
    RiderFinder riderFinder;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    @DisplayName("성공 - 반경 내 라이더들을 거리순으로 조회")
    void findNearby_Success() {
        // given
        redisTemplate.delete("rider:location");

        redisTemplate.opsForGeo().add(
                "rider:location",
                new Point(126.9780, 37.5665),
                "1"
        );

        redisTemplate.opsForGeo().add(
                "rider:location",
                new Point(126.9900, 37.5700),
                "2"
        );

        OrderCreatedEvent.PickupLocationDto pickup =
                new OrderCreatedEvent.PickupLocationDto(
                        37.5665,
                        126.9780,
                        "서울시 마포구"
                );

        // when
        List<RiderCandidate> candidates = riderFinder.findNearby(pickup, 5.0);

        // then
        assertThat(candidates).isNotEmpty();
        assertThat(candidates)
                .extracting(RiderCandidate::getRiderId)
                .contains(1L, 2L);

        assertThat(candidates.get(0).getDistanceKm())
                .isLessThanOrEqualTo(candidates.get(1).getDistanceKm());
    }
}
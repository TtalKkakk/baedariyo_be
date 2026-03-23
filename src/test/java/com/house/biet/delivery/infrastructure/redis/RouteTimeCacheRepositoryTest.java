package com.house.biet.delivery.infrastructure.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RouteTimeCacheRepositoryTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Test
    @DisplayName("reads numeric redis values safely")
    void getReadsNumericValueSafely() {
        // given
        RouteTimeCacheRepository repository = new RouteTimeCacheRepository(redisTemplate);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("route-key")).willReturn(15L);

        // when
        Integer result = repository.get("route-key");

        // then
        assertThat(result).isEqualTo(15);
    }

    @Test
    @DisplayName("reads string redis values safely")
    void getReadsStringValueSafely() {
        // given
        RouteTimeCacheRepository repository = new RouteTimeCacheRepository(redisTemplate);
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get("route-key")).willReturn("18");

        // when
        Integer result = repository.get("route-key");

        // then
        assertThat(result).isEqualTo(18);
    }
}

package com.house.biet.support;

import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

public abstract class BaseRepositoryTest {

    private static final AtomicLong SEQ = new AtomicLong(1);

    protected static final LocalDateTime BASE_TIME =
            LocalDateTime.of(2026, 1, 1, 12, 0);

    protected Long nextId() {
        return SEQ.getAndIncrement();
    }

    protected LocalDateTime baseTime() {
        return BASE_TIME;
    }

    @BeforeEach
    void resetSequence() {
        SEQ.set(1);
    }
}

package com.house.biet.store.query.dto;

import com.house.biet.common.domain.enums.StoreCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StoreSearchRequestDtoTest {

    @Test
    @DisplayName("성공 - page가 null이면 기본값 0을 반환한다")
    void getPageOrDefault_Success_DefaultValue() {
        // Given
        StoreSearchRequestDto dto = new StoreSearchRequestDto(
                "치킨",
                StoreCategory.CHICKEN,
                37.123,
                127.123,
                null,
                10
        );

        // When
        int result = dto.getPageOrDefault();

        // Then
        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("성공 - page 값이 존재하면 해당 값을 반환한다")
    void getPageOrDefault_Success_ReturnValue() {
        // Given
        StoreSearchRequestDto dto = new StoreSearchRequestDto(
                "치킨",
                StoreCategory.CHICKEN,
                37.123,
                127.123,
                3,
                10
        );

        // When
        int result = dto.getPageOrDefault();

        // Then
        assertThat(result).isEqualTo(3);
    }

    @Test
    @DisplayName("성공 - size가 null이면 기본값 10을 반환한다")
    void getSizeOrDefault_Success_DefaultValue() {
        // Given
        StoreSearchRequestDto dto = new StoreSearchRequestDto(
                "피자",
                StoreCategory.PIZZA,
                37.123,
                127.123,
                1,
                null
        );

        // When
        int result = dto.getSizeOrDefault();

        // Then
        assertThat(result).isEqualTo(10);
    }

    @Test
    @DisplayName("성공 - size 값이 존재하면 해당 값을 반환한다")
    void getSizeOrDefault_Success_ReturnValue() {
        // Given
        StoreSearchRequestDto dto = new StoreSearchRequestDto(
                "피자",
                StoreCategory.PIZZA,
                37.123,
                127.123,
                1,
                25
        );

        // When
        int result = dto.getSizeOrDefault();

        // Then
        assertThat(result).isEqualTo(25);
    }
}
package com.house.biet.store.command.application.dto;

import com.house.biet.common.domain.enums.StoreCategory;
import com.house.biet.common.domain.vo.Money;
import com.house.biet.store.command.domain.vo.*;
import jakarta.validation.constraints.*;

public record StoreCreateRequestDto(
        @NotBlank
        String storeName,

        @NotNull
        StoreCategory storeCategory,

        String thumbnailUrl,           // nullable 가능

        BusinessHours businessHours,   // nullable 가능

        StoreOperationInfo operationInfo, // nullable 가능

        @NotNull
        Money minimumOrderAmount,

        @NotNull
        Money deliveryFee
) {}
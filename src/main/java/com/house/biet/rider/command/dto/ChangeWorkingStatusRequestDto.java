package com.house.biet.rider.command.dto;

import com.house.biet.common.domain.enums.RiderWorkingStatus;
import jakarta.validation.constraints.NotNull;

public record ChangeWorkingStatusRequestDto(

        @NotNull(message = "근무 상태는 필수입니다.")
        RiderWorkingStatus workingStatus

) {}
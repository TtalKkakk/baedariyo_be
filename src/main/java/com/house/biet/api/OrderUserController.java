package com.house.biet.api;

import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.order.command.domain.application.OrderUserFacade;
import com.house.biet.order.command.domain.dto.OrderCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders/rider")
public class OrderUserController {

    private final OrderUserFacade orderUserFacade;

    @PostMapping("/assign")
    public ResponseEntity<CustomApiResponse<Void>> create(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody OrderCreateRequestDto requestDto
    ) {
        orderUserFacade.createOrderAndNotify(principal.accountId(), requestDto);
        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.ORDER_RIDER_ASSIGN_SUCCESS)
        );
    }
}

package com.house.biet.api;

import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.order.command.application.OrderRiderFacade;
import com.house.biet.order.command.domain.dto.OrderRiderAssignRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders/users")
public class OrderRiderController {

    private OrderRiderFacade orderRiderFacade;

    @PostMapping("/create")
    public ResponseEntity<CustomApiResponse<Void>> assignRider(
            @AuthenticationPrincipal AuthPrincipal principal,
            @RequestBody OrderRiderAssignRequestDto requestDto
    ) {
        orderRiderFacade.assignRider(
                principal.accountId(),
                requestDto.orderId()
        );

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.ORDER_CREATE_SUCCESS)
        );
    }
}

package com.house.biet.api;

import com.house.biet.application.flow.DeliveryOrderFacade;
import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.common.domain.enums.OrderStatus;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import com.house.biet.order.query.application.OrderQueryService;
import com.house.biet.order.query.application.dto.OrderSummaryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveries")
public class DeliveryRiderController {

    private final DeliveryOrderFacade deliveryOrderFacade;
    private final OrderQueryService orderQueryService;

    @GetMapping("/available-orders")
    public ResponseEntity<CustomApiResponse<List<OrderSummaryResponseDto>>> getAvailableOrders() {
        List<OrderSummaryResponseDto> response = orderQueryService.findOrderSummariesByOrderStatus(OrderStatus.PAID);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.DELIVERY_GET_LIST_SUCCESS, response)
        );
    }

    /**
     * ?쇱씠?붿쓣 ?좊떦?쒕떎
     *
     * @param principal ?몄쬆 ?ъ슜???뺣낫
     * @param orderId 二쇰Ц ?앸퀎??
     * @return assignRider 寃곌낵
     */
    @PostMapping("/{orderId}/assign")
    public ResponseEntity<CustomApiResponse<Void>> assignRider(
            @AuthenticationPrincipal AuthPrincipal principal,
            @PathVariable Long orderId
    ) {
        deliveryOrderFacade.assignRider(orderId, principal.accountId());

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.DELIVERY_RIDER_ASSIGN_SUCCESS)
        );
    }


    /**
     * 諛곕떖???쒖옉?쒕떎
     *
     * @param orderId 二쇰Ц ?앸퀎??
     * @return startDelivery 寃곌낵
     */
    @PostMapping("/{orderId}/start")
    public ResponseEntity<CustomApiResponse<Void>> startDelivery(@PathVariable Long orderId) {
        deliveryOrderFacade.startDelivery(orderId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.DELIVERY_START_SUCCESS)
        );
    }

    /**
     * 諛곕떖???꾨즺?쒕떎
     *
     * @param orderId 二쇰Ц ?앸퀎??
     * @return completeDelivery 寃곌낵
     */
    @PostMapping("/{orderId}/complete")
    public ResponseEntity<CustomApiResponse<Void>> completeDelivery(@PathVariable Long orderId) {
        deliveryOrderFacade.completeDelivery(orderId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.DELIVERY_COMPLETE_SUCCESS)
        );
    }

}

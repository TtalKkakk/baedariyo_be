package com.house.biet.api;

import com.house.biet.application.flow.DeliveryOrderFacade;
import com.house.biet.auth.infrastructure.security.AuthPrincipal;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveries")
public class DeliveryRiderController {

    private final DeliveryOrderFacade deliveryOrderFacade;

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


    @PostMapping("/{orderId}/start")
    public ResponseEntity<CustomApiResponse<Void>> startDelivery(@PathVariable Long orderId) {
        deliveryOrderFacade.startDelivery(orderId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.DELIVERY_START_SUCCESS)
        );
    }

    @PostMapping("/{orderId}/complete")
    public ResponseEntity<CustomApiResponse<Void>> completeDelivery(@PathVariable Long orderId) {
        deliveryOrderFacade.completeDelivery(orderId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.DELIVERY_COMPLETE_SUCCESS)
        );
    }

}

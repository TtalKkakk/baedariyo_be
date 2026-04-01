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

    /**
     * 라이더을 할당한다
     *
     * @param principal 인증 사용자 정보
     * @param orderId 주문 식별자
     * @return assignRider 결과
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
     * 배달을 시작한다
     *
     * @param orderId 주문 식별자
     * @return startDelivery 결과
     */
    @PostMapping("/{orderId}/start")
    public ResponseEntity<CustomApiResponse<Void>> startDelivery(@PathVariable Long orderId) {
        deliveryOrderFacade.startDelivery(orderId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.DELIVERY_START_SUCCESS)
        );
    }

    /**
     * 배달을 완료한다
     *
     * @param orderId 주문 식별자
     * @return completeDelivery 결과
     */
    @PostMapping("/{orderId}/complete")
    public ResponseEntity<CustomApiResponse<Void>> completeDelivery(@PathVariable Long orderId) {
        deliveryOrderFacade.completeDelivery(orderId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.DELIVERY_COMPLETE_SUCCESS)
        );
    }

}

package com.house.biet.api;

import com.house.biet.delivery.command.application.DeliveryLocationService;
import com.house.biet.delivery.command.application.DeliveryService;
import com.house.biet.delivery.query.dto.DeliveryLocationResponseDto;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deliveries")
public class DeliveryLocationController {

    private final DeliveryLocationService deliveryLocationService;

    /**
     * 현재 배달 위치 조회
     */
    @GetMapping("/{orderId}/location")
    public ResponseEntity<CustomApiResponse<DeliveryLocationResponseDto>> getLocation(
            @PathVariable Long orderId
    ) {
        DeliveryLocationResponseDto responseDto = deliveryLocationService.getCurrentLocation(orderId);

        return ResponseEntity.ok(
                CustomApiResponse.success(SuccessCode.DELIVERY_GET_SUCCESS, responseDto)
        );
    }
}

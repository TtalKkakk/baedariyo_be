package com.house.biet.api;

import com.house.biet.delivery.command.application.DeliveryLocationService;
import com.house.biet.delivery.command.application.DeliveryService;
import com.house.biet.delivery.query.dto.DeliveryLocationResponseDto;
import com.house.biet.delivery.query.dto.UpdateLocationRequestDto;
import com.house.biet.global.response.CustomApiResponse;
import com.house.biet.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/deliveries")
public class DeliveryLocationController {

    private final DeliveryLocationService deliveryLocationService;

    /**
     * 현재 배달 위치 조회
     *
     * @param orderId 주문 식별자
     * @return getLocation 결과
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


    /**
     * 위치을 변경한다
     *
     * @param orderId 주문 식별자
     * @param requestDto 요청 정보
     * @return updateLocation 결과
     */
    @PostMapping("/{orderId}/location")
    public ResponseEntity<Void> updateLocation(
            @PathVariable Long orderId,
            @RequestBody UpdateLocationRequestDto requestDto
    ) {
        deliveryLocationService.updateLocation(
                orderId,
                requestDto.latitude(),
                requestDto.longitude()
        );

        return ResponseEntity.noContent().build();
    }
}

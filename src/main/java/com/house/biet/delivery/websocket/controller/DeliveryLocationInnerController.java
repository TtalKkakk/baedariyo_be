package com.house.biet.delivery.websocket.controller;

import com.house.biet.delivery.command.application.DeliveryLocationService;
import com.house.biet.delivery.websocket.dto.DeliveryLocationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class DeliveryLocationInnerController {

    private final DeliveryLocationService locationService;

    /**
     * 위치을 처리한다
     *
     * @param message message 값
     * @param principal 인증 사용자 정보
     */
    @MessageMapping("/location")
    public void receiveLocation(
            DeliveryLocationMessage message,
            Principal principal
    ) {
        Long accountId = Long.valueOf(principal.getName());

        locationService.handleLocation(message, accountId);
    }
}
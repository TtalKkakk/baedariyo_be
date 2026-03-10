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

    @MessageMapping("/location")
    public void receiveLocation(
            DeliveryLocationMessage message,
            Principal principal
    ) {
        Long accountId = Long.valueOf(principal.getName());

        locationService.handleLocation(message, accountId);
    }
}
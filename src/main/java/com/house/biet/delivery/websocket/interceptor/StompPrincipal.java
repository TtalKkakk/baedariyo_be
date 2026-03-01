package com.house.biet.delivery.websocket.interceptor;

import java.security.Principal;

public record StompPrincipal(Long userId) implements Principal {

    @Override
    public String getName() {
        return userId.toString();
    }
}
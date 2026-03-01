package com.house.biet.delivery.websocket.interceptor;


import com.house.biet.auth.infrastructure.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.*;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompJwtChannelInterceptor implements ChannelInterceptor {

    private final JwtProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String bearerToken = accessor.getFirstNativeHeader("Authorization");

            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                throw new IllegalArgumentException("WebSocket 인증 토큰이 없습니다.");
            }

            String token = bearerToken.substring(7);

            if (!jwtTokenProvider.validateToken(token)) {
                throw new IllegalArgumentException("유효하지 않은 WebSocket 토큰입니다.");
            }

            Long accountId = jwtTokenProvider.getAccountId(token);

            accessor.setUser(new StompPrincipal(accountId));
        }

        return message;
    }
}
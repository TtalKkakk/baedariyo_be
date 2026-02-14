package com.house.biet.order.command.application;

import com.house.biet.order.command.application.port.RiderCandidate;
import com.house.biet.order.command.application.port.RiderFinder;
import com.house.biet.order.command.domain.event.OrderCreatedEvent;
import com.house.biet.order.command.infrastructure.messaging.rider.RiderCallSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderNotificationService {

    private final RiderFinder riderFinder;
    private final RiderCallSender riderCallSender;

    /**
     * 주문 생성 이벤트를 받아서 주변 라이더에게 호출 메시지를 보냄
     */
    public void notifyRiders(OrderCreatedEvent event) {
        // 주변 5km 내의 라이더 찾기
        List<RiderCandidate> candidates
                = riderFinder.findNearby(event.getPickupLocationDto(), 5.0);

        // 찾은 라이더들에게 메시지 보내기
        for (RiderCandidate candidate: candidates) {
            riderCallSender.send(candidate, event);
        }
    }
}

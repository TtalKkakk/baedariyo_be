package com.house.biet.support.fake;

import com.house.biet.order.command.application.port.RiderCandidate;
import com.house.biet.order.command.application.port.RiderFinder;
import com.house.biet.order.command.domain.event.OrderCreatedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("test")
public class FakeRiderFinder implements RiderFinder {

    @Override
    public List<RiderCandidate> findNearby(OrderCreatedEvent.PickupLocationDto pickupLocationDto, double radiusKm) {
        return List.of(
                new RiderCandidate(1L, 0.5),
                new RiderCandidate(2L, 1.2)
        );
    }
}

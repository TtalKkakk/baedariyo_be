package com.house.biet.delivery.command;

import com.house.biet.delivery.command.domain.aggregate.Delivery;
import com.house.biet.delivery.command.repository.DeliveryRepositoryJpaAdapter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(DeliveryRepositoryJpaAdapter.class)
@ActiveProfiles("test")
class DeliveryRepositoryTest {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    @DisplayName("성공 - Delivery 저장")
    void save_Success() {
        // given
        Long orderId = 2L;
        Delivery delivery = Delivery.create(orderId);

        // when
        Delivery saved = deliveryRepository.save(delivery);

        // then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getOrderId()).isEqualTo(orderId);
    }

    @Test
    @DisplayName("성공 - orderId로 Delivery 조회")
    void findByOrderId_Success() {
        // given
        Long orderId = 10L;
        Delivery delivery = Delivery.create(orderId);
        deliveryRepository.save(delivery);

        // when
        Optional<Delivery> result = deliveryRepository.findByOrderId(orderId);

        // then
        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("성공 - 존재하지 않는 orderId 조회 시 empty 반환")
    void findByOrderId_Success_NotFoundDelivery() {
        // when
        Optional<Delivery> result =
                deliveryRepository.findByOrderId(999L);

        // then
        assertThat(result).isEmpty();
    }
}
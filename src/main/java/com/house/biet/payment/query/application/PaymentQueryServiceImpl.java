package com.house.biet.payment.query.application;

import com.house.biet.common.domain.enums.PaymentStatus;
import com.house.biet.payment.command.domain.aggregate.Payment;
import com.house.biet.payment.query.PaymentQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentQueryServiceImpl implements PaymentQueryService {

    private final PaymentQueryRepository paymentQueryRepository;

    public List<Payment> findByStatus(PaymentStatus status) {
        return paymentQueryRepository.findAllByStatus(status);
    }

    public List<Payment> findByOrderId(Long orderId) {
        return paymentQueryRepository.findAllByOrderId(orderId);
    }
}
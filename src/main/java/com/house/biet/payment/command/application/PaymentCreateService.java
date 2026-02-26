package com.house.biet.payment.command.application;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.payment.command.PaymentService;
import com.house.biet.user.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCreateService {

    private final UserQueryService userQueryService;
    private final PaymentService paymentService;

    public Long createPayment(Long orderId, Money money, String paymentKey, Long accountId) {
        Long userId = userQueryService.getUserIdByAccountId(accountId);

        return paymentService.createPayment(orderId, userId, money, paymentKey);
    }

}

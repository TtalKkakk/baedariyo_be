package com.house.biet.payment.command.application;

import com.house.biet.common.domain.vo.Money;
import com.house.biet.user.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentCreateService {

    private final UserQueryService userQueryService;
    private final PaymentService paymentService;

    /**
     * 결제을 생성한다
     *
     * @param orderId 주문 식별자
     * @param money money 값
     * @param paymentKey 결제 키
     * @param accountId 계정 식별자
     * @return 처리 결과 값
     */
    public Long createPayment(Long orderId, Money money, String paymentKey, Long accountId) {
        Long userId = userQueryService.getUserIdByAccountId(accountId);

        return paymentService.createPayment(orderId, userId, money, paymentKey);
    }

}

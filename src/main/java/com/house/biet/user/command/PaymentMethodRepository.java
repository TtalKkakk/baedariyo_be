package com.house.biet.user.command;

import com.house.biet.user.command.domain.entity.UserPaymentMethod;

import java.util.List;
import java.util.Optional;

public interface PaymentMethodRepository {

    /**
     * 결제수단을 저장한다
     *
     * @param paymentMethod 결제수단 값
     * @return save 결과
     */
    UserPaymentMethod save(UserPaymentMethod paymentMethod);

    /**
     * 식별자로 결제수단을 조회한다
     *
     * @param id 결제수단 식별자
     * @return 조회 결과
     */
    Optional<UserPaymentMethod> findById(Long id);

    /**
     * 사용자 식별자로 모든 결제수단을 조회한다
     *
     * @param userId 사용자 식별자
     * @return 조회 결과
     */
    List<UserPaymentMethod> findAllByUserId(Long userId);

    /**
     * 식별자로 결제수단을 삭제한다
     *
     * @param id 결제수단 식별자
     */
    void deleteById(Long id);
}

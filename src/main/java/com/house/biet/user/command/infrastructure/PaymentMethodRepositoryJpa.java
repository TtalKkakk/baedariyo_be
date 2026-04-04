package com.house.biet.user.command.infrastructure;

import com.house.biet.user.command.domain.entity.UserPaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentMethodRepositoryJpa extends JpaRepository<UserPaymentMethod, Long> {

    /**
     * 사용자 식별자로 모든 결제수단을 조회한다
     *
     * @param userId 사용자 식별자
     * @return 조회 결과
     */
    List<UserPaymentMethod> findAllByUserId(Long userId);
}

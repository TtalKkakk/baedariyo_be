package com.house.biet.user.command.infrastructure;

import com.house.biet.user.command.PaymentMethodRepository;
import com.house.biet.user.command.domain.entity.UserPaymentMethod;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentMethodRepositoryJpaAdapter implements PaymentMethodRepository {

    private final PaymentMethodRepositoryJpa paymentMethodRepositoryJpa;

    @Override
    public UserPaymentMethod save(UserPaymentMethod paymentMethod) {
        return paymentMethodRepositoryJpa.save(paymentMethod);
    }

    @Override
    public Optional<UserPaymentMethod> findById(Long id) {
        return paymentMethodRepositoryJpa.findById(id);
    }

    @Override
    public List<UserPaymentMethod> findAllByUserId(Long userId) {
        return paymentMethodRepositoryJpa.findAllByUserId(userId);
    }

    @Override
    public void deleteById(Long id) {
        paymentMethodRepositoryJpa.deleteById(id);
    }
}

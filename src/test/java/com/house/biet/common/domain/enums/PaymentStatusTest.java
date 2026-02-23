package com.house.biet.common.domain.enums;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class PaymentStatusTest {

    private static final Map<PaymentStatus, Set<PaymentStatus>> ALLOWED_TRANSITIONS = Map.of(
            PaymentStatus.READY, Set.of(PaymentStatus.REQUESTED),
            PaymentStatus.REQUESTED, Set.of(PaymentStatus.APPROVED, PaymentStatus.FAILED),
            PaymentStatus.APPROVED, Set.of(PaymentStatus.CANCELED),
            PaymentStatus.FAILED, Set.of(),
            PaymentStatus.CANCELED, Set.of()
    );

    @Test
    @DisplayName("성공 - 허용된 모든 상태 전이는 예외 없이 검증되고 상태는 변경되지 않는다")
    void validateTransition_Success_AllAllowedTransitions() {

        // given
        for (PaymentStatus from : PaymentStatus.values()) {
            Set<PaymentStatus> allowedTargets = ALLOWED_TRANSITIONS.get(from);

            for (PaymentStatus target : allowedTargets) {
                // when
                from.validateTransition(target);

                // then
                assertThat(from)
                        .as("validateTransition 은 상태를 변경하면 안 된다: %s -> %s", from, target)
                        .isEqualTo(from);
            }
        }
    }

    @Test
    @DisplayName("실패 - 허용되지 않은 모든 전이는 INVALID_PAYMENT_STATUS_TRANSITION 예외가 발생해야 한다")
    void validateTransition_Error_AllDisallowedTransitions() {

        // given
        for (PaymentStatus from : PaymentStatus.values()) {
            Set<PaymentStatus> allowedTargets = ALLOWED_TRANSITIONS.get(from);

            for (PaymentStatus target : PaymentStatus.values()) {
                if (!allowedTargets.contains(target)) {
                    // when & then
                    assertThatThrownBy(() -> from.validateTransition(target))
                            .as("실패해야 하는 전이인데 성공함: %s -> %s", from, target)
                            .isInstanceOf(CustomException.class)
                            .hasMessage(ErrorCode.INVALID_PAYMENT_STATUS_TRANSITION.getMessage());
                }
            }
        }
    }
}
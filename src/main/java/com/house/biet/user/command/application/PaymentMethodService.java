package com.house.biet.user.command.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.user.command.PaymentMethodRepository;
import com.house.biet.user.command.UserRepository;
import com.house.biet.user.command.domain.aggregate.User;
import com.house.biet.user.command.domain.entity.UserPaymentMethod;
import com.house.biet.user.command.dto.PaymentMethodResponseDto;
import com.house.biet.user.command.dto.StartRegistrationResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentMethodService {

    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    private static final ConcurrentHashMap<String, Long> pendingTokens = new ConcurrentHashMap<>();

    /**
     * 사용자의 결제수단 목록을 조회한다.
     *
     * @param accountId 계정 ID (인증 주체)
     * @return 결제수단 목록
     */
    @Transactional(readOnly = true)
    public List<PaymentMethodResponseDto> getPaymentMethods(Long accountId) {
        Long userId = getUserIdOrThrow(accountId);

        return paymentMethodRepository.findAllByUserId(userId).stream()
                .map(PaymentMethodResponseDto::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 결제수단을 등록한다.
     *
     * @param accountId   계정 ID (인증 주체)
     * @param billingKey  빌링키
     * @param cardNumber  카드 번호 (마스킹 처리)
     * @return 등록된 결제수단 정보
     */
    public PaymentMethodResponseDto addPaymentMethod(Long accountId, String billingKey, String cardNumber) {
        Long userId = getUserIdOrThrow(accountId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String cardName = deriveCardName(cardNumber);

        UserPaymentMethod paymentMethod = UserPaymentMethod.create(user, billingKey, cardNumber, cardName, false);

        UserPaymentMethod saved = paymentMethodRepository.save(paymentMethod);

        return PaymentMethodResponseDto.from(saved);
    }

    /**
     * 사용자의 결제수단을 삭제한다.
     *
     * @param accountId       계정 ID (인증 주체)
     * @param paymentMethodId 삭제할 결제수단 식별자
     */
    public void deletePaymentMethod(Long accountId, Long paymentMethodId) {
        Long userId = getUserIdOrThrow(accountId);

        UserPaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_METHOD_NOT_FOUND));

        if (!paymentMethod.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        paymentMethodRepository.deleteById(paymentMethodId);
    }

    /**
     * 카드 등록 프로세스를 시작하고 UUID 토큰을 반환한다.
     *
     * @param accountId 계정 ID (인증 주체)
     * @return UUID 토큰
     */
    public StartRegistrationResponseDto startRegistration(Long accountId) {
        Long userId = getUserIdOrThrow(accountId);

        String token = UUID.randomUUID().toString();
        pendingTokens.put(token, userId);

        return new StartRegistrationResponseDto(token);
    }

    private Long getUserIdOrThrow(Long accountId) {
        return userRepository.findUserIdByAccountId(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    private String deriveCardName(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "등록 카드";
        }

        String prefix = cardNumber.replaceAll("[^0-9]", "");
        if (prefix.length() < 4) {
            return "등록 카드";
        }

        int firstFour = Integer.parseInt(prefix.substring(0, 4));

        if (firstFour >= 4000 && firstFour <= 4999) {
            return "Visa";
        } else if ((firstFour >= 5100 && firstFour <= 5599) || (firstFour >= 2221 && firstFour <= 2720)) {
            return "Mastercard";
        } else if (firstFour == 3528 || firstFour == 3589) {
            return "JCB";
        } else if (firstFour == 3411 || firstFour == 3412 || firstFour == 3414 || firstFour == 3415) {
            return "American Express";
        } else {
            return "등록 카드";
        }
    }
}

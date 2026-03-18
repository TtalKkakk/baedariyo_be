package com.house.biet.user.command.application;

import com.house.biet.user.query.UserQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자(User) 도메인의 Command 유스케이스를 조합하여 처리하는 Facade.
 *
 * <p>
 * 이 클래스는 외부 요청(Controller)으로부터 전달받은 식별자(AccountId)를
 * 내부 도메인에서 사용하는 식별자(UserId)로 변환한 뒤,
 * 실제 비즈니스 로직을 수행하는 {@link UserService}에 위임한다.
 * </p>
 *
 * <p>
 * 주요 책임은 다음과 같다:
 * <ul>
 *     <li>Account 기반 요청을 User 도메인에 맞게 변환한다.</li>
 *     <li>여러 Application Service 간의 흐름을 조합(Orchestration)한다.</li>
 *     <li>트랜잭션 경계를 유지하며 Command 작업을 수행한다.</li>
 * </ul>
 * </p>
 *
 * <p>
 * 이 Facade는 "외부 인터페이스 계층과 도메인 계층 사이의 번역기" 역할을 수행하며,
 * 인증/인가 단위(Account)와 도메인 단위(User)의 분리를 유지한다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandFacade {

    private final UserService userService;
    private final UserQueryService userQueryService;

    /**
     * 사용자 닉네임을 변경한다.
     *
     * <p>
     * Account ID를 기반으로 User를 조회한 뒤,
     * 새로운 닉네임으로 변경한다.
     * </p>
     *
     * @param accountId        계정 ID (인증 주체)
     * @param newNicknameValue 변경할 닉네임 값
     */
    public void changeNickname(Long accountId, String newNicknameValue) {
        Long userId = getUserId(accountId);

        userService.changeNicknameByUserId(userId, newNicknameValue);
    }

    /**
     * 사용자 전화번호를 변경한다.
     *
     * <p>
     * Account ID를 기반으로 User를 조회한 뒤,
     * 새로운 전화번호로 변경한다.
     * </p>
     *
     * @param accountId             계정 ID (인증 주체)
     * @param newPhoneNumberValue   변경할 전화번호 값
     */
    public void changePhoneNumber(Long accountId, String newPhoneNumberValue) {
        Long userId = getUserId(accountId);

        userService.changePhoneNumberByUserId(userId, newPhoneNumberValue);
    }

    /**
     * 사용자에게 새로운 배송지를 추가한다.
     *
     * <p>
     * Account ID를 기반으로 User를 조회한 뒤,
     * 전달받은 주소 정보를 이용하여 배송지를 생성한다.
     * </p>
     *
     * <p>
     * 기본 배송지로 설정할 경우 기존 기본 배송지는 자동으로 해제된다.
     * 첫 번째 배송지인 경우, 자동으로 기본 배송지로 설정된다.
     * </p>
     *
     * @param accountId     계정 ID (인증 주체)
     * @param roadAddress   도로명 주소
     * @param jibunAddress  지번 주소
     * @param detailAddress 상세 주소
     * @param aliasValue    주소 별칭 (예: 집, 회사)
     * @param isDefault     기본 배송지 여부
     */
    public void addAddress(
            Long accountId,
            String roadAddress,
            String jibunAddress,
            String detailAddress,
            String aliasValue,
            boolean isDefault
    ) {
        Long userId = getUserId(accountId);

        userService.addAddress(
                userId,
                roadAddress,
                jibunAddress,
                detailAddress,
                aliasValue,
                isDefault
        );
    }

    /**
     * 사용자의 기본 배송지를 변경한다.
     *
     * <p>
     * Account ID를 기반으로 User를 조회한 뒤,
     * 지정된 addressId를 기본 배송지로 설정한다.
     * 기존 기본 배송지는 자동으로 해제된다.
     * </p>
     *
     * @param accountId 계정 ID (인증 주체)
     * @param addressAlias 기본 배송지로 설정할 주소 별명
     */
    public void changeDefaultAddress(Long accountId, String addressAlias) {
        Long userId = getUserId(accountId);

        userService.changeDefaultAddress(userId, addressAlias);
    }

    /**
     * 사용자의 배송지를 삭제한다.
     *
     * <p>
     * Account ID를 기반으로 User를 조회한 뒤,
     * 지정된 addressId에 해당하는 배송지를 삭제한다.
     * </p>
     *
     * <p>
     * 삭제 대상이 기본 배송지인 경우,
     * 남아있는 배송지 중 하나가 자동으로 기본 배송지로 설정된다.
     * </p>
     *
     * @param accountId 계정 ID (인증 주체)
     * @param addressAlias 삭제할 배송지 별명
     */
    public void removeAddress(Long accountId, String addressAlias) {
        Long userId = getUserId(accountId);

        userService.removeAddress(userId, addressAlias);
    }

    /**
     * Account ID를 기반으로 User ID를 조회한다.
     *
     * <p>
     * 외부 계층에서 사용하는 식별자(Account)와
     * 내부 도메인에서 사용하는 식별자(User)를 연결하는 역할을 한다.
     * </p>
     *
     * @param accountId 계정 ID
     * @return 사용자 ID
     */
    private Long getUserId(Long accountId) {
        return userQueryService.getUserIdByAccountId(accountId);
    }
}
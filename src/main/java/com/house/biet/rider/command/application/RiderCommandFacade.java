package com.house.biet.rider.command.application;

import com.house.biet.common.domain.enums.VehicleType;
import com.house.biet.rider.query.RiderQueryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 라이더 관련 Command 요청을 처리하는 Facade 클래스입니다.
 *
 * <p>
 * AccountId를 기반으로 RiderId를 조회한 뒤,
 * 실제 비즈니스 로직은 {@link RiderService}에 위임합니다.
 * </p>
 *
 * <p>
 * 본 Facade는 상태 값을 직접 전달하지 않고,
 * 도메인 행위 단위의 메서드를 호출하여 상태 전이를 수행합니다.
 * </p>
 *
 * <p>
 * 역할:
 * <ul>
 *     <li>Account → Rider 식별자 변환</li>
 *     <li>Command 서비스로 위임</li>
 * </ul>
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RiderCommandFacade {

    private final RiderService riderService;
    private final RiderQueryService riderQueryService;

    /**
     * 라이더 닉네임을 변경합니다.
     *
     * @param accountId 계정 ID
     * @param newNicknameValue 변경할 닉네임
     */
    public void changeNicknameByRiderId(Long accountId, String newNicknameValue) {
        Long riderId = riderQueryService.getRiderIdByAccountId(accountId);
        riderService.changeNicknameByRiderId(riderId, newNicknameValue);
    }

    /**
     * 라이더 전화번호를 변경합니다.
     *
     * @param accountId 계정 ID
     * @param newPhoneNumberValue 변경할 전화번호
     */
    public void changePhoneNumberByRiderId(Long accountId, String newPhoneNumberValue) {
        Long riderId = riderQueryService.getRiderIdByAccountId(accountId);

        riderService.changePhoneNumberByRiderId(riderId, newPhoneNumberValue);
    }

    /**
     * 라이더의 차량 유형을 변경합니다.
     *
     * @param accountId 계정 ID
     * @param newVehicleType 변경할 차량 유형
     */
    public void changeVehicleType(Long accountId, VehicleType newVehicleType) {
        Long riderId = riderQueryService.getRiderIdByAccountId(accountId);

        riderService.changeVehicleType(riderId, newVehicleType);
    }

    /**
     * 라이더를 온라인 상태로 전환합니다.
     *
     * @param accountId 계정 ID
     */
    public void goOnline(Long accountId) {
        Long riderId = riderQueryService.getRiderIdByAccountId(accountId);

        riderService.goOnline(riderId);
    }

    /**
     * 배달을 시작하여 WORKING 상태로 전환합니다.
     *
     * @param accountId 계정 ID
     */
    public void startDelivery(Long accountId) {
        Long riderId = riderQueryService.getRiderIdByAccountId(accountId);

        riderService.startDelivery(riderId);
    }

    /**
     * 배달을 완료하여 ONLINE 상태로 전환합니다.
     *
     * @param accountId 계정 ID
     */
    public void completeDelivery(Long accountId) {
        Long riderId = riderQueryService.getRiderIdByAccountId(accountId);

        riderService.completeDelivery(riderId);
    }

    /**
     * 라이더를 오프라인 상태로 전환합니다.
     *
     * @param accountId 계정 ID
     */
    public void goOffline(Long accountId) {
        Long riderId = riderQueryService.getRiderIdByAccountId(accountId);

        riderService.goOffline(riderId);
    }
}
package com.house.biet.rider.command.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.member.command.domain.vo.PhoneNumber;
import com.house.biet.rider.command.RiderRepository;
import com.house.biet.rider.command.domain.entity.Rider;
import com.house.biet.common.domain.enums.VehicleType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 라이더(Command) 도메인의 상태 변경 및 정보 수정을 담당하는 서비스
 *
 * <p>
 * 본 서비스는 라이더 생성 및 라이더의 주요 정보(닉네임, 전화번호, 이동수단) 수정과,
 * 라이더의 근무 상태 전이를 "행위 기반"으로 수행한다.
 * </p>
 *
 * <p>
 * 근무 상태는 단순 값 변경이 아닌 도메인 규칙에 따른 상태 전이로 관리되며,
 * 허용되지 않은 상태 변경 시 {@link CustomException}이 발생한다.
 * </p>
 *
 * <p>
 * 모든 로직은 트랜잭션 내에서 수행되며,
 * 라이더가 존재하지 않을 경우 {@link ErrorCode#RIDER_NOT_FOUND} 예외가 발생한다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RiderService {

    private final RiderRepository riderRepository;

    /**
     * 라이더를 신규 생성한다
     *
     * @param account          계정 정보
     * @param realNameValue    라이더 실명
     * @param nicknameValue    라이더 닉네임
     * @param phoneNumberValue 라이더 전화번호
     * @param vehicleType      이동 수단 타입
     */
    public void save(Account account, String realNameValue, String nicknameValue, String phoneNumberValue, VehicleType vehicleType) {
        Rider rider = Rider.create(account, realNameValue, nicknameValue, phoneNumberValue, vehicleType);
        riderRepository.save(rider);
    }

    /**
     * 라이더의 닉네임을 변경한다
     *
     * @param riderId 라이더 식별자
     * @param newNicknameValue 변경할 닉네임
     * @throws CustomException 라이더가 존재하지 않는 경우
     */
    public void changeNicknameByRiderId(Long riderId, String newNicknameValue) {
        Rider rider = getRiderOrThrow(riderId);

        rider.changeNickname(new Nickname(newNicknameValue));
    }

    /**
     * 라이더의 전화번호를 변경한다
     *
     * @param riderId 라이더 식별자
     * @param newPhoneNumberValue 변경할 전화번호
     * @throws CustomException 라이더가 존재하지 않는 경우
     */
    public void changePhoneNumberByRiderId(Long riderId, String newPhoneNumberValue) {
        Rider rider = getRiderOrThrow(riderId);

        rider.changePhoneNumber(new PhoneNumber(newPhoneNumberValue));
    }

    /**
     * 라이더의 이동 수단을 변경한다
     *
     * @param riderId 라이더 식별자
     * @param newVehicleType 변경할 이동 수단 타입
     * @throws CustomException 라이더가 존재하지 않는 경우
     */
    public void changeVehicleType(Long riderId, VehicleType newVehicleType) {
        Rider rider = getRiderOrThrow(riderId);

        rider.changeVehicleType(newVehicleType);
    }

    /**
     * 라이더를 온라인 상태로 전환한다
     *
     * <p>
     * 현재 상태가 OFFLINE인 경우에만 ONLINE으로 전환 가능하다.
     * </p>
     *
     * @param riderId 라이더 식별자
     * @throws CustomException
     * - 라이더가 존재하지 않는 경우
     * - 허용되지 않은 상태 전이인 경우
     */
    public void goOnline(Long riderId) {
        Rider rider = getRiderOrThrow(riderId);

        rider.goOnline();
    }

    /**
     * 배달을 시작하여 WORKING 상태로 전환한다
     *
     * <p>
     * 현재 상태가 ONLINE인 경우에만 배달을 시작할 수 있다.
     * </p>
     *
     * @param riderId 라이더 식별자
     * @throws CustomException
     * - 라이더가 존재하지 않는 경우
     * - ONLINE 상태가 아닌 경우
     */
    public void startDelivery(Long riderId) {
        Rider rider = getRiderOrThrow(riderId);

        rider.startDelivery();
    }

    /**
     * 배달을 완료하여 ONLINE 상태로 전환한다
     *
     * <p>
     * 현재 상태가 WORKING인 경우에만 완료할 수 있다.
     * </p>
     *
     * @param riderId 라이더 식별자
     * @throws CustomException
     * - 라이더가 존재하지 않는 경우
     * - WORKING 상태가 아닌 경우
     */
    public void completeDelivery(Long riderId) {
        Rider rider = getRiderOrThrow(riderId);

        rider.completeDelivery();
    }

    /**
     * 라이더를 오프라인 상태로 전환한다
     *
     * <p>
     * 현재 상태가 ONLINE인 경우에만 OFFLINE으로 전환 가능하다.
     * </p>
     *
     * @param riderId 라이더 식별자
     * @throws CustomException
     * - 라이더가 존재하지 않는 경우
     * - ONLINE 상태가 아닌 경우
     */
    public void goOffline(Long riderId) {
        Rider rider = getRiderOrThrow(riderId);

        rider.goOffline();
    }

    /**
     * 라이더 조회 (존재하지 않을 경우 예외 발생)
     *
     * @param riderId 라이더 식별자
     * @return Rider 엔티티
     * @throws CustomException 라이더가 존재하지 않는 경우
     */
    private Rider getRiderOrThrow(Long riderId) {
        return riderRepository.findById(riderId)
                .orElseThrow(() -> new CustomException(ErrorCode.RIDER_NOT_FOUND));
    }
}
package com.house.biet.rider.command.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.member.command.domain.vo.PhoneNumber;
import com.house.biet.rider.command.RiderRepository;
import com.house.biet.rider.command.domain.entity.Rider;
import com.house.biet.common.domain.enums.RiderWorkingStatus;
import com.house.biet.common.domain.enums.VehicleType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 라이더(Command) 도메인에 대한 비즈니스 로직을 담당하는 서비스
 *
 * <p>
 * 라이더 생성 및 라이더의 주요 상태(닉네임, 전화번호, 이동수단, 근무 상태)를 변경하는
 * Command 전용 서비스이다.
 * </p>
 *
 * <p>
 * 모든 변경 로직은 트랜잭션 내에서 수행되며,
 * 라이더가 존재하지 않을 경우 {@link CustomException}을 발생시킨다.
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
     * @param account         저장된 계좌 정보
     * @param realNameValue   라이더 실명
     * @param nicknameValue   라이더 닉네임
     * @param phoneNumberValue 라이더 전화번호
     * @param vehicleType     이동 수단 타입
     */
    public void save(Account account, String realNameValue, String nicknameValue, String phoneNumberValue, VehicleType vehicleType) {
        Rider rider = Rider.create(account, realNameValue, nicknameValue, phoneNumberValue, vehicleType);

        riderRepository.save(rider);
    }

    /**
     * 라이더 ID를 기준으로 닉네임을 변경한다
     *
     * @param riderId 라이더 식별자
     * @param newNicknameValue 변경할 닉네임
     * @throws CustomException 라이더가 존재하지 않는 경우
     */
    public void changeNicknameByRiderId(Long riderId, String newNicknameValue) {
        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new CustomException(ErrorCode.RIDER_NOT_FOUND));

        rider.changeNickname(new Nickname(newNicknameValue));
    }

    /**
     * 라이더 ID를 기준으로 전화번호를 변경한다
     *
     * @param riderId 라이더 식별자
     * @param newPhoneNumberValue 변경할 전화번호
     * @throws CustomException 라이더가 존재하지 않는 경우
     */
    public void changePhoneNumberByRiderId(Long riderId, String newPhoneNumberValue) {
        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new CustomException(ErrorCode.RIDER_NOT_FOUND));

        rider.changePhoneNumber(new PhoneNumber(newPhoneNumberValue));
    }

    /**
     * 라이더 ID를 기준으로 이동 수단을 변경한다
     *
     * @param riderId 라이더 식별자
     * @param newVehicleType 변경할 이동 수단 타입
     * @throws CustomException 라이더가 존재하지 않는 경우
     */
    public void changeVehicleType(Long riderId, VehicleType newVehicleType) {
        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new CustomException(ErrorCode.RIDER_NOT_FOUND));

        rider.changeVehicleType(newVehicleType);
    }

    /**
     * 라이더 ID를 기준으로 근무 상태를 변경한다
     *
     * @param riderId 라이더 식별자
     * @param newRiderWorkingStatus 변경할 근무 상태
     * @throws CustomException 라이더가 존재하지 않는 경우
     */
    public void changeRiderWorkingStatus(Long riderId, RiderWorkingStatus newRiderWorkingStatus) {
        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new CustomException(ErrorCode.RIDER_NOT_FOUND));

        rider.changeRiderWorkingStatus(newRiderWorkingStatus);
    }

    /**
     * 라이더 로그인 시 근무 상태를 ONLINE으로 전환한다
     *
     * <p>
     * 현재 상태가 OFFLINE인 경우에만 ONLINE으로 변경한다
     * </p>
     *
     * @param riderId 라이더 식별자
     */
    public void markOnlineIfOffline(Long riderId) {
        Rider rider = riderRepository.findById(riderId)
                .orElseThrow(() -> new CustomException(ErrorCode.RIDER_NOT_FOUND));

        if (rider.getRiderWorkingStatus() == RiderWorkingStatus.OFFLINE) {
            rider.changeRiderWorkingStatus(RiderWorkingStatus.ONLINE);
        }
    }

}
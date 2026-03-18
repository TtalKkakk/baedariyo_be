package com.house.biet.user.command.application;

import com.house.biet.common.domain.vo.Address;
import com.house.biet.global.geocoding.application.GeocodingService;
import com.house.biet.global.geocoding.dto.GeoPoint;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.member.command.domain.vo.PhoneNumber;
import com.house.biet.store.command.domain.vo.GeoLocation;
import com.house.biet.user.command.UserRepository;
import com.house.biet.user.command.domain.aggregate.User;
import com.house.biet.user.command.domain.vo.AddressAlias;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 사용자(User) 도메인의 상태 변경을 담당하는 Application Service.
 *
 * <p>
 * 이 서비스는 사용자 생성 및 사용자 정보 변경과 같은
 * 쓰기(Command) 유스케이스를 처리한다.
 * </p>
 *
 * <p>
 * 주요 책임은 다음과 같다:
 * <ul>
 *     <li>사용자 생성 시 Address를 기반으로 좌표(GeoLocation)를 생성한다.</li>
 *     <li>도메인 객체(User)의 생성 및 상태 변경을 위임한다.</li>
 *     <li>트랜잭션 경계를 관리하고 Repository를 통해 영속화한다.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Address → GeoLocation 변환은 외부 Geocoding API를 사용하며,
 * 이는 Application Layer의 책임으로 처리한다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final GeocodingService geocodingService;

    /**
     * 새로운 사용자를 생성하고 저장한다.
     *
     * <p>
     * 사용자 생성 시, 전달받은 주소 정보를 기반으로 Geocoding을 수행하여
     * 위도/경도(GeoLocation)를 생성한 뒤 기본 배송지로 등록한다.
     * </p>
     *
     * <p>
     * 생성된 User는 항상 하나의 기본 배송지를 가지는 유효한 상태를 보장한다.
     * </p>
     *
     * @param account           연관된 계정 정보 (필수)
     * @param realNameValue     사용자 실명 값
     * @param nicknameValue     사용자 닉네임 값
     * @param phoneNumberValue  사용자 전화번호 값
     * @param roadAddress       도로명 주소
     * @param jibunAddress      지번 주소
     * @param detailAddress     상세 주소
     * @param aliasValue        주소 별칭 (예: 집, 회사)
     *
     * @return 생성 및 저장된 {@link User}
     *
     * @throws CustomException 다음과 같은 경우 발생한다:
     * <ul>
     *     <li>주소 형식이 올바르지 않은 경우</li>
     *     <li>Geocoding에 실패한 경우</li>
     * </ul>
     */
    public User save(
            Account account,
            String realNameValue,
            String nicknameValue,
            String phoneNumberValue,
            String roadAddress,
            String jibunAddress,
            String detailAddress,
            String aliasValue
    ) {

        // 1. Address 생성
        Address address = new Address(roadAddress, jibunAddress, detailAddress);

        // 2. Geocoding
        GeoPoint point = geocodingService.geocode(address.getRoadAddress());

        // 3. GeoLocation 생성
        var geoLocation = new GeoLocation(
                point.latitude(),
                point.longitude()
        );

        // 4. AddressAlias 생성
        AddressAlias alias = new AddressAlias(aliasValue);

        // 4. User 생성 (Address 포함)
        User user = User.create(
                account,
                realNameValue,
                nicknameValue,
                phoneNumberValue,
                address,
                geoLocation,
                alias
        );

        return userRepository.save(user);
    }

    /**
     * 닉네임을 기준으로 사용자를 조회한 뒤 닉네임을 변경한다.
     *
     * <p>
     * 주어진 닉네임을 가진 사용자를 조회한 후,
     * 새로운 닉네임으로 변경한다.
     * </p>
     *
     * @param newNicknameValue 변경할 닉네임 값
     *
     * @throws CustomException 다음과 같은 경우 발생한다:
     * <ul>
     *     <li>해당 닉네임을 가진 사용자가 존재하지 않는 경우</li>
     * </ul>
     */
    public void changeNickname(String newNicknameValue) {
        Nickname newNickname = new Nickname(newNicknameValue);

        User user = userRepository.findByNickname(newNickname)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.changeNickname(newNickname);
    }

    /**
     * 사용자 ID를 기준으로 사용자를 조회한 뒤 닉네임을 변경한다.
     *
     * @param userId            사용자 ID
     * @param newNicknameValue  변경할 닉네임 값
     *
     * @throws CustomException 다음과 같은 경우 발생한다:
     * <ul>
     *     <li>사용자를 찾을 수 없는 경우</li>
     * </ul>
     */
    public void changeNicknameByUserId(Long userId, String newNicknameValue) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.changeNickname(new Nickname(newNicknameValue));
    }

    /**
     * 사용자 ID를 기준으로 사용자를 조회한 뒤 전화번호를 변경한다.
     *
     * @param userId               사용자 ID
     * @param newPhoneNumberValue  변경할 전화번호 값
     *
     * @throws CustomException 다음과 같은 경우 발생한다:
     * <ul>
     *     <li>사용자를 찾을 수 없는 경우</li>
     * </ul>
     */
    public void changePhoneNumberByUserId(Long userId, String newPhoneNumberValue) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.changePhoneNumber(new PhoneNumber(newPhoneNumberValue));
    }
}
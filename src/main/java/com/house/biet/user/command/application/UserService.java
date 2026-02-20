package com.house.biet.user.command.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.member.command.domain.vo.PhoneNumber;
import com.house.biet.user.command.UserRepository;
import com.house.biet.user.command.domain.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 사용자(User) 도메인의 상태 변경을 담당하는 Application Service.
 *
 * <p>
 * 이 서비스는 사용자 생성 및 사용자 정보 변경과 같은
 * 쓰기(Command) 성격의 유스케이스를 처리한다.
 * </p>
 *
 * <p>
 * 도메인 객체의 생성 및 상태 변경은 {@link User} 엔티티에 위임하며,
 * 이 서비스는 트랜잭션 경계 및 저장소 접근을 담당한다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    /**
     * 새로운 사용자를 생성하고 저장한다.
     *
     * @param account         저장된 계좌 정보
     * @param realNameValue   사용자 실명
     * @param nicknameValue   사용자 닉네임
     * @param phoneNumberValue 사용자 전화번호
     */
    public User save(Account account, String realNameValue, String nicknameValue, String phoneNumberValue) {
        User user = User.create(account, realNameValue, nicknameValue, phoneNumberValue);

        return userRepository.save(user);
    }

    /**
     * 닉네임을 기준으로 사용자를 조회한 뒤 닉네임을 변경한다.
     *
     * <p>
     * 주어진 닉네임을 가진 사용자가 존재하지 않을 경우
     * {@link CustomException}이 발생한다.
     * </p>
     *
     * @param newNicknameValue 변경할 닉네임 값
     * @throws CustomException 사용자를 찾을 수 없는 경우
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
     * @param userId           사용자 ID
     * @param newNicknameValue 변경할 닉네임 값
     * @throws CustomException 사용자를 찾을 수 없는 경우
     */
    public void changeNicknameByUserId(Long userId, String newNicknameValue) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.changeNickname(new Nickname(newNicknameValue));
    }

    /**
     * 사용자 ID를 기준으로 사용자를 조회한 뒤 전화번호를 변경한다.
     *
     * @param userId              사용자 ID
     * @param newPhoneNumberValue 변경할 전화번호 값
     * @throws CustomException 사용자를 찾을 수 없는 경우
     */
    public void changePhoneNumberByUserId(Long userId, String newPhoneNumberValue) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        user.changePhoneNumber(new PhoneNumber(newPhoneNumberValue));
    }
}

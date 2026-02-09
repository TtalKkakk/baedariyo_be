package com.house.biet.rider.command;

import com.house.biet.global.vo.UserRole;
import com.house.biet.member.command.AccountRepository;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import com.house.biet.member.command.domain.vo.Password;
import com.house.biet.member.command.infrastructure.AccountRepositoryJpaAdapter;
import com.house.biet.rider.command.domain.entity.Rider;
import com.house.biet.rider.command.domain.vo.RiderWorkingStatus;
import com.house.biet.rider.command.domain.vo.VehicleType;
import com.house.biet.rider.command.infrastructure.RiderRepositoryJpaAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        RiderRepositoryJpaAdapter.class,
        AccountRepositoryJpaAdapter.class
})@ActiveProfiles("test")
class RiderRepositoryTest {

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private AccountRepository accountRepository;

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    String givenEmail = "abc@xyz.com";
    String givenPassword = UUID.randomUUID().toString().substring(1, 30);

    String givenRealName = "<REAL_NAME>";
    String givenNickname = "<NICKNAME>";
    String givenPhoneNumber = "010-1112-1111";
    VehicleType givenVehicleType = VehicleType.MOTORCYCLE;

    Rider rider;

    @BeforeEach
    void setup() {
        Account savedAccount = accountRepository.save(Account.signup(
                new Email(givenEmail),
                Password.encrypt(givenPassword, ENCODER),
                UserRole.USER
        ));

        rider = Rider.create(savedAccount, givenRealName, givenNickname, givenPhoneNumber, givenVehicleType);
    }

    @Test
    @DisplayName("성공 - Rider 데이터 저장 성공")
    void saveRider_Success() {
        // when
        Rider savedRider = riderRepository.save(rider);

        // then
        assertThat(savedRider).isNotNull();
        assertThat(savedRider.getRealName().getValue()).isEqualTo(givenRealName);
        assertThat(savedRider.getNickname().getValue()).isEqualTo(givenNickname);
        assertThat(savedRider.getVehicleType()).isEqualTo(givenVehicleType);
        assertThat(savedRider.getRiderWorkingStatus()).isEqualTo(RiderWorkingStatus.OFFLINE);
    }

    @Test
    @DisplayName("성공 - id로 Rider 찾기")
    void findById_Success() {
        // given
        Rider savedRider = riderRepository.save(rider);

        // when
        Optional<Rider> foundRider = riderRepository.findById(savedRider.getId());

        // then
        assertThat(foundRider).isNotEmpty();
        assertThat(foundRider.get().getNickname()).isEqualTo(savedRider.getNickname());
    }

    @Test
    @DisplayName("성공 - 없는 id로 찾을 경우 비어있는지 확인")
    void findByNotExistRiderId_Success() {
        // given
        riderRepository.save(rider);
        Long notExistId = -1L;

        // when
        Optional<Rider> foundRider = riderRepository.findById(notExistId);

        // then
        assertThat(foundRider).isEmpty();
    }

    @Test
    @DisplayName("성공 - 존재 여부 확인")
    void existsById_Success() {
        // given
        Rider savedRider = riderRepository.save(rider);

        // when
        boolean isExists = riderRepository.existsById(savedRider.getId());

        // then
        assertThat(isExists).isTrue();
    }

    @Test
    @DisplayName("성공 - 미존재 여부 확인")
    void notExistsById_Success() {
        // given
        riderRepository.save(rider);
        Long notExistsId = -1L;

        // when
        boolean isExists = riderRepository.existsById(notExistsId);

        // then
        assertThat(isExists).isFalse();
    }

    @Test
    @DisplayName("성공 - 닉네임으로 id 찾기")
    void findRiderIdByNickname_Success() {
        // given
        riderRepository.save(rider);

        // when
        Optional<Long> foundRiderId = riderRepository.findRiderIdByNickname(rider.getNickname().getValue());

        // then
        assertThat(foundRiderId).isPresent();
        assertThat(foundRiderId.get()).isEqualTo(rider.getId());
    }

    @Test
    @DisplayName("성공 - 없는 nickname 으로 조회시 null")
    void findRiderIdyNickname_Success_NotExistsNickname() {
        // given
        riderRepository.save(rider);
        String notExistsNicknameValue = "<NOT_EXISTS_NICKNAME>";

        // when
        Optional<Long> foundRiderId = riderRepository.findRiderIdByNickname(notExistsNicknameValue);

        // then
        assertThat(foundRiderId).isEmpty();
    }

    @Test
    @DisplayName("성공 - accountId로 riderId 찾기")
    void findRiderIdByAccountId_Success() {
        // given
        riderRepository.save(rider);
        Long accountId = rider.getAccount().getId();

        // when
        Optional<Long> foundRiderId = riderRepository.findRiderIdByAccountId(accountId);

        // then
        assertThat(foundRiderId).isPresent();
        assertThat(foundRiderId.get()).isEqualTo(rider.getId());
    }

    @Test
    @DisplayName("성공 - 없는 accountId로 riderId 찾기")
    void findRiderIdByAccountId_Success_NotFoundAccountId() {
        // given
        riderRepository.save(rider);
        Long notExistsAccountId = -1L;

        // when
        Optional<Long> foundRiderId = riderRepository.findRiderIdByAccountId(notExistsAccountId);

        // then
        assertThat(foundRiderId).isEmpty();
    }
}
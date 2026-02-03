package com.house.biet.rider.command;

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
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(RiderRepositoryJpaAdapter.class)
@ActiveProfiles("test")
class RiderRepositoryTest {

    @Autowired
    private RiderRepository riderRepository;

    String givenRealName = "<REAL_NAME>";
    String givenNickname = "<NICKNAME>";
    String givenPhoneNumber = "010-1112-1111";
    VehicleType givenVehicleType = VehicleType.MOTORCYCLE;

    Rider rider;

    @BeforeEach
    void setup() {
        rider = Rider.create(givenRealName, givenNickname, givenPhoneNumber, givenVehicleType);
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
}
package com.house.biet.rider.command.domain.entity;

import com.house.biet.global.jpa.BaseTimeEntity;
import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.member.command.domain.vo.PhoneNumber;
import com.house.biet.member.command.domain.vo.RealName;
import com.house.biet.common.domain.enums.RiderWorkingStatus;
import com.house.biet.common.domain.enums.VehicleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "riders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Rider extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "real_name", nullable = false)
    )
    private RealName realName;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "nickname", nullable = false, unique = true)
    )
    private Nickname nickname;


    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "phoneNumber", nullable = false, unique = true)
    )
    private PhoneNumber phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RiderWorkingStatus riderWorkingStatus;

    public static Rider create(Account account, String realName, String nickname, String phoneNumber, VehicleType vehicleType) {
        return new Rider(
                null,
                account,
                new RealName(realName),
                new Nickname(nickname),
                new PhoneNumber(phoneNumber),
                vehicleType,
                RiderWorkingStatus.OFFLINE
        );
    }

    public void changeNickname(Nickname nickname) {
        this.nickname = nickname;
    }

    public void changePhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void changeVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    // 근무 상태 변경

    public void goOnline() {
        if (this.riderWorkingStatus != RiderWorkingStatus.OFFLINE) {
            throw new CustomException(ErrorCode.RIDER_MUST_BE_OFFLINE_TO_GO_ONLINE);
        }
        this.riderWorkingStatus = RiderWorkingStatus.ONLINE;
    }

    public void startDelivery() {
        if (this.riderWorkingStatus != RiderWorkingStatus.ONLINE) {
            throw new CustomException(ErrorCode.RIDER_MUST_BE_ONLINE_TO_START_WORK);
        }
        this.riderWorkingStatus = RiderWorkingStatus.WORKING;
    }

    public void completeDelivery() {
        if (this.riderWorkingStatus != RiderWorkingStatus.WORKING) {
            throw new CustomException(ErrorCode.RIDER_MUST_BE_WORKING_TO_COMPLETE);
        }
        this.riderWorkingStatus = RiderWorkingStatus.ONLINE;
    }

    public void goOffline() {
        if (this.riderWorkingStatus != RiderWorkingStatus.ONLINE) {
            throw new CustomException(ErrorCode.RIDER_MUST_BE_ONLINE_TO_GO_OFFLINE);
        }
        this.riderWorkingStatus = RiderWorkingStatus.OFFLINE;
    }
}

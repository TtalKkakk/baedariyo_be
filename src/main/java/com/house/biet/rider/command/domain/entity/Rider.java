package com.house.biet.rider.command.domain.entity;

import com.house.biet.global.jpa.BaseTimeEntity;
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

    public void changeRiderWorkingStatus(RiderWorkingStatus riderWorkingStatus) {
        this.riderWorkingStatus = riderWorkingStatus;
    }
}

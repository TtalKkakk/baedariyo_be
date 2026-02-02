package com.house.biet.rider.command.domain.entity;

import com.house.biet.global.jpa.BaseTimeEntity;
import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.member.command.domain.vo.PhoneNumber;
import com.house.biet.member.command.domain.vo.RealName;
import com.house.biet.rider.command.domain.vo.RiderWorkingStatus;
import com.house.biet.rider.command.domain.vo.VehicleType;
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

    public static Rider create(String realName, String nickname, String phoneNumber, VehicleType vehicleType, RiderWorkingStatus riderWorkingStatus) {
        return new Rider(
                null,
                new RealName(realName),
                new Nickname(nickname),
                new PhoneNumber(phoneNumber),
                vehicleType,
                riderWorkingStatus
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

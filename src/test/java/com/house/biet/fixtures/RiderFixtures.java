package com.house.biet.fixtures;

import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.rider.command.domain.entity.Rider;
import com.house.biet.rider.command.domain.vo.VehicleType;

public class RiderFixtures {

    private RiderFixtures() {}

    public static Rider rider(Account riderAccount) {
        return Rider.create(
                riderAccount,
                "테스트라이더",
                "testRider",
                "010-9999-9999",
                VehicleType.MOTORCYCLE
        );
    }
}

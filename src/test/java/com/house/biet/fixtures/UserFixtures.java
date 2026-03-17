package com.house.biet.fixtures;

import com.house.biet.common.domain.vo.Address;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.store.command.domain.vo.GeoLocation;
import com.house.biet.user.command.domain.aggregate.User;

public class UserFixtures {

    private UserFixtures() {}

    public static User user(Account userAccount) {
        Address address = AddressFixtures.mapoAddress();

        GeoLocation geoLocation = new GeoLocation(
                37.5563,
                126.9220
        );

        return User.create(
                userAccount,
                "테스트유저",
                "testUser",
                "010-0000-0000",
                address,
                geoLocation,
                "집"
        );
    }
}
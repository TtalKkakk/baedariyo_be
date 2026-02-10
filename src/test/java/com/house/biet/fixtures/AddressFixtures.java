package com.house.biet.fixtures;

import com.house.biet.order.command.domain.vo.Address;

public class AddressFixtures {

    public static Address mapoAddress() {
        return new Address(
                "서울특별시 마포구 월드컵북로 396",
                "서울특별시 마포구 상암동 1605",
                "101동 1001호"
        );
    }

    public static Address gangnamAddress() {
        return new Address(
                "서울특별시 강남구 테헤란로 123",
                "서울특별시 강남구 역삼동 123-45",
                "B동 202호"
        );
    }

    public static String blank() {
        return " ";
    }

    public static String validRoadAddress() {
        return "서울특별시 마포구 월드컵북로 396";
    }

    public static String validJibunAddress() {
        return "서울특별시 마포구 상암동 1605";
    }

    public static String validDetailAddress() {
        return "101동 1001호";
    }
}

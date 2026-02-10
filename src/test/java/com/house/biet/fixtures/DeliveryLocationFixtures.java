package com.house.biet.fixtures;

import com.house.biet.order.command.domain.vo.DeliveryLocation;

public class DeliveryLocationFixtures {

    public static DeliveryLocation seoulMapo() {
        return new DeliveryLocation(
                37.566535,
                126.977969,
                "SEOUL_MAPO"
        );
    }

    public static DeliveryLocation seoulGangnam() {
        return new DeliveryLocation(
                37.497942,
                127.027621,
                "SEOUL_GANGNAM"
        );
    }

    public static double validLatitude() {
        return 37.566535;
    }

    public static double validLongitude() {
        return 126.977969;
    }

    public static String validRegion() {
        return "SEOUL_MAPO";
    }
}

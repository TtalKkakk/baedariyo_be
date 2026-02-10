package com.house.biet.fixtures;

import com.house.biet.order.command.domain.vo.DeliveryInstruction;

public class DeliveryInstructionFixtures {

    public static DeliveryInstruction fullInstruction() {
        return new DeliveryInstruction(
                "문 앞에 놔주세요",
                "1234",
                "조심히 와주세요"
        );
    }

    public static DeliveryInstruction emptyInstruction() {
        return new DeliveryInstruction(null, null, null);
    }

    public static DeliveryInstruction onlyRiderRequest() {
        return new DeliveryInstruction(
                "경비실에 맡겨주세요",
                null,
                null
        );
    }
}

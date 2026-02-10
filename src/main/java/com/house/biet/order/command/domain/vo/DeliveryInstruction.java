package com.house.biet.order.command.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryInstruction {

    private String riderRequest;
    private String frontDoorPassword;
    private String notice;

    public DeliveryInstruction(String riderRequest,
                               String frontDoorPassword,
                               String notice) {
        this.riderRequest = riderRequest;
        this.frontDoorPassword = frontDoorPassword;
        this.notice = notice;
    }
}

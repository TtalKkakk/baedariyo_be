package com.house.biet.order.command.domain.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DeliveryInstructionTest {

    @Test
    @DisplayName("성공 - DeliveryInstruction 생성")
    void createDeliveryInstruction_Success() {
        // given
        String riderRequest = "문 앞에 놔주세요";
        String password = "1234";
        String notice = "문 앞에 개조심";

        // when
        DeliveryInstruction instruction =
                new DeliveryInstruction(riderRequest, password, notice);

        // then
        assertThat(instruction.getRiderRequest()).isEqualTo(riderRequest);
        assertThat(instruction.getFrontDoorPassword()).isEqualTo(password);
        assertThat(instruction.getNotice()).isEqualTo(notice);
    }

    @Test
    @DisplayName("성공 - 전부 null로 생성")
    void createDeliveryInstruction_Success_NullArgs() {
        // when
        DeliveryInstruction instruction =
                new DeliveryInstruction(null, null, null);

        // then
        assertThat(instruction).isNotNull();
    }
}

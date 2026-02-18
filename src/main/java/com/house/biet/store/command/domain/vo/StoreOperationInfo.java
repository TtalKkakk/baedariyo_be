package com.house.biet.store.command.domain.vo;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreOperationInfo {

    private String storeInfo;      // 매장 정보
    private String originInfo;     // 원산지 정보

    public StoreOperationInfo(String storeInfo, String originInfo) {
        if (storeInfo != null && storeInfo.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_STORE_INFO_FORMAT);
        }
        if (originInfo != null && originInfo.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_OPERATION_INFO_FORMAT);
        }

        this.storeInfo = storeInfo;
        this.originInfo = originInfo;
    }

    public static StoreOperationInfo of(String storeInfo, String originInfo) {
        if (storeInfo == null && originInfo == null) {
            return null; // Store에서 선택적으로 사용 가능
        }
        return new StoreOperationInfo(storeInfo, originInfo);
    }
}

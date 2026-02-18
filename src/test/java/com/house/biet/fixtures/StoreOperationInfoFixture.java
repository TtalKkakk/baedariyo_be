package com.house.biet.fixtures;

import com.house.biet.store.command.domain.vo.StoreOperationInfo;

public class StoreOperationInfoFixture {

    private String storeInfo = "매장 내 식사 가능";
    private String originInfo = "돼지고기: 국내산";

    public static StoreOperationInfoFixture aStoreOperationInfo() {
        return new StoreOperationInfoFixture();
    }

    public StoreOperationInfoFixture withStoreInfo(String storeInfo) {
        this.storeInfo = storeInfo;
        return this;
    }

    public StoreOperationInfoFixture withOriginInfo(String originInfo) {
        this.originInfo = originInfo;
        return this;
    }

    public StoreOperationInfo build() {
        return new StoreOperationInfo(storeInfo, originInfo);
    }
}

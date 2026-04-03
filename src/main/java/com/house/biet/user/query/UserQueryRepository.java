package com.house.biet.user.query;

import com.house.biet.user.query.application.dto.UserProfileResponseDto;

public interface UserQueryRepository {

    /**
     * accountID로 userId 찾기
     * 
     * @param accountId 인증된 accountId
     * @return accountId에 해당된 userId
     */
    Long findUserIdByAccountId(Long accountId);
    
    /**
     * 유저 정보 찾기
     * 
     * @param accountId 인증된 accountId
     * @return UserProfileResponseDto 유저 정보
     */
    UserProfileResponseDto getUserProfile(Long accountId);
}

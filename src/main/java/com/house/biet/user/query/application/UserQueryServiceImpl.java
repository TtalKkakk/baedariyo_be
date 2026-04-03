package com.house.biet.user.query.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.user.command.UserRepository;
import com.house.biet.user.query.application.dto.UserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    @Override
    public Long getUserIdByAccountId(Long accountId) {
        return userRepository.findUserIdByAccountId(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public UserProfileResponseDto getUserProfile(Long accountId) {
        return userRepository;
    }
}

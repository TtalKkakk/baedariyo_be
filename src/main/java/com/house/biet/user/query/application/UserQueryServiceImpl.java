package com.house.biet.user.query.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.user.command.UserRepository;
import com.house.biet.user.query.UserQueryRepository;
import com.house.biet.user.query.application.dto.UserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryServiceImpl implements UserQueryService {

    private final UserQueryRepository userQueryRepository;

    @Override
    public Long getUserIdByAccountId(Long accountId) {
        return Optional.ofNullable(
                userQueryRepository.findUserIdByAccountId(accountId)
        ).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public UserProfileResponseDto getUserProfile(Long accountId) {
        return userQueryRepository.getUserProfile(accountId);
    }
}

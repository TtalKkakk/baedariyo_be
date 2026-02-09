package com.house.biet.rider.query;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.rider.command.RiderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RiderQueryServiceImpl implements RiderQueryService {

    private final RiderRepository riderRepository;

    @Override
    public Long getRiderIdByNickname(String nicknameValue) {
        return riderRepository.findRiderIdByNickname(nicknameValue)
                .orElseThrow(() -> new CustomException(ErrorCode.RIDER_NOT_FOUND));
    }

    @Override
    public Long getRiderIdByAccountId(Long accountId) {
        return riderRepository.findRiderIdByAccountId(accountId)
                .orElseThrow(() -> new CustomException(ErrorCode.RIDER_NOT_FOUND));
    }
}

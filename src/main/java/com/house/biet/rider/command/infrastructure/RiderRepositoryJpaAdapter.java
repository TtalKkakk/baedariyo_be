package com.house.biet.rider.command.infrastructure;

import com.house.biet.member.command.domain.vo.Nickname;
import com.house.biet.rider.command.RiderRepository;
import com.house.biet.rider.command.domain.entity.Rider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RiderRepositoryJpaAdapter implements RiderRepository {

    private final RiderRepositoryJpa repositoryJpa;

    @Override
    public Rider save(Rider rider) {
        return repositoryJpa.save(rider);
    }

    @Override
    public Optional<Rider> findById(Long riderId) {
        return repositoryJpa.findById(riderId);
    }

    @Override
    public boolean existsById(Long riderId) {
        return repositoryJpa.existsById(riderId);
    }

    @Override
    public Optional<Long> findRiderIdByNickname(String nickname) {
        return repositoryJpa.findIdByNickname(nickname);
    }

    @Override
    public Optional<Long> findRiderIdByAccountId(Long accountId) {
        return repositoryJpa.findRiderIdByAccountId(accountId);
    }
}

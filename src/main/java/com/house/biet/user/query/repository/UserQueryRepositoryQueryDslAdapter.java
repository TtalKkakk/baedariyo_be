package com.house.biet.user.query.repository;

import com.house.biet.user.query.UserQueryRepository;
import com.house.biet.user.query.application.dto.UserProfileResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.house.biet.member.command.domain.entity.QAccount.account;
import static com.house.biet.user.command.domain.aggregate.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserQueryRepositoryQueryDslAdapter implements UserQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public UserProfileResponseDto getUserProfile(Long accountId) {
        return queryFactory
                .select(Projections.constructor(
                        UserProfileResponseDto.class,
                        user.nickname.value,
                        user.phoneNumber.value,
                        account.email.value
                ))
                .from(user)
                .join(user.account, account)
                .where(account.id.eq(accountId))
                .fetchOne();
    }
}

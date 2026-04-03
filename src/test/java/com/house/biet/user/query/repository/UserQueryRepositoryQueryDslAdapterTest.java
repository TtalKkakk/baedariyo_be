package com.house.biet.user.query.repository;

import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.common.domain.vo.Address;
import com.house.biet.fixtures.AccountFixtures;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.store.command.domain.vo.GeoLocation;
import com.house.biet.support.config.QueryDslTestConfig;
import com.house.biet.user.command.domain.aggregate.User;
import com.house.biet.user.command.domain.vo.AddressAlias;
import com.house.biet.user.query.UserQueryRepository;
import com.house.biet.user.query.application.dto.UserProfileResponseDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        UserQueryRepositoryQueryDslAdapter.class,
        QueryDslTestConfig.class
})
class UserQueryRepositoryQueryDslAdapterTest {

    @Autowired
    private UserQueryRepository userQueryRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("성공 - accountId로 유저 프로필 조회")
    void getUserProfile_Success() {
        // given
        Account account = AccountFixtures.account(UserRole.USER);
        em.persist(account);

        User user = User.create(
                account,
                "홍길동",
                "TestNickname",
                "010-0000-0001",
                new Address("서울", "강남", "12345"),
                new GeoLocation(37.123, 127.123),
                new AddressAlias("집")
        );
        em.persist(user);

        em.flush();
        em.clear();

        // when
        UserProfileResponseDto result =
                userQueryRepository.getUserProfile(account.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.nickname()).isEqualTo(user.getNickname().getValue());
        assertThat(result.email()).isEqualTo(account.getEmail().getValue());
        assertThat(result.phoneNumber()).isEqualTo(user.getPhoneNumber().getValue());
    }
}
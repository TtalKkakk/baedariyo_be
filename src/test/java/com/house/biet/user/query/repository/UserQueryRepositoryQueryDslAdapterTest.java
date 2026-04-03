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
import org.junit.jupiter.api.Nested;
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

    @Nested
    @DisplayName("findUserIdByAccountId")
    class FindUserIdByAccountId {

        @Test
        @DisplayName("성공 - accountId로 userId 조회")
        void findUserIdByAccountId_Success() {
            // given
            Account account = AccountFixtures.account(UserRole.USER);
            em.persist(account);

            User user = User.create(
                    account,
                    "홍길동",
                    "User1",
                    "010-0000-0001",
                    new Address("서울", "강남", "12345"),
                    new GeoLocation(37.123, 127.123),
                    new AddressAlias("집")
            );
            em.persist(user);

            em.flush();
            em.clear();

            // when
            Long result = userQueryRepository.findUserIdByAccountId(account.getId());

            // then
            assertThat(result).isEqualTo(user.getId());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 accountId로 조회 시 null 반환")
        void findUserIdByAccountId_Error_NotFound() {
            // given
            Long invalidAccountId = 999L;

            // when
            Long result = userQueryRepository.findUserIdByAccountId(invalidAccountId);

            // then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("getUserProfile")
    class GetUserProfile {

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
            assertThat(result.nickname()).isEqualTo("TestNickname");
            assertThat(result.phoneNumber()).isEqualTo("010-0000-0001");
            assertThat(result.email()).isEqualTo(account.getEmail().getValue());
        }

        @Test
        @DisplayName("실패 - 존재하지 않는 accountId로 조회 시 null 반환")
        void getUserProfile_Error_NotFound() {
            // given
            Long invalidAccountId = 999L;

            // when
            UserProfileResponseDto result =
                    userQueryRepository.getUserProfile(invalidAccountId);

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("실패 - account는 존재하지만 user가 없는 경우")
        void getUserProfile_Error_UserNotFound() {
            // given
            Account account = AccountFixtures.account(UserRole.USER);
            em.persist(account);

            em.flush();
            em.clear();

            // when
            UserProfileResponseDto result =
                    userQueryRepository.getUserProfile(account.getId());

            // then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("성공 - 여러 유저 중 accountId에 해당하는 유저만 조회")
        void getUserProfile_Success_JoinValidation() {
            // given
            Account account1 = AccountFixtures.account(UserRole.USER);
            Account account2 = AccountFixtures.account(UserRole.USER);

            em.persist(account1);
            em.persist(account2);

            User user1 = User.create(
                    account1,
                    "홍길동",
                    "User1",
                    "010-0000-0001",
                    new Address("서울", "강남", "12345"),
                    new GeoLocation(37.123, 127.123),
                    new AddressAlias("집")
            );

            User user2 = User.create(
                    account2,
                    "김철수",
                    "User2",
                    "010-0000-0002",
                    new Address("부산", "해운대", "54321"),
                    new GeoLocation(35.123, 129.123),
                    new AddressAlias("회사")
            );

            em.persist(user1);
            em.persist(user2);

            em.flush();
            em.clear();

            // when
            UserProfileResponseDto result =
                    userQueryRepository.getUserProfile(account1.getId());

            // then
            assertThat(result).isNotNull();
            assertThat(result.nickname()).isEqualTo("User1");
        }
    }
}
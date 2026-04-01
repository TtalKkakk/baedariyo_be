package com.house.biet.member.command.infrastructure;

import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepositoryJpa
        extends JpaRepository<Account, Long> {

    /**
     * Email Role을 처리한다
     *
     * @param email email 값
     * @param role role 값
     * @return existsByEmailAndRole 결과
     */
    boolean existsByEmailAndRole(Email email, UserRole role);
    /**
     * Email Role을 조회한다
     *
     * @param email email 값
     * @param role role 값
     * @return 조회 결과
     */
    Optional<Account> findByEmailAndRole(Email email, UserRole role);
}

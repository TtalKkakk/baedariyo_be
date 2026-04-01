package com.house.biet.member.command;

import com.house.biet.common.domain.enums.UserRole;
import com.house.biet.member.command.domain.entity.Account;
import com.house.biet.member.command.domain.vo.Email;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {

    /**
     * 대상을 저장한다
     *
     * @param account 계정 정보
     * @return save 결과
     */
    Account save(Account account);

    /**
     * 식별자을 조회한다
     *
     * @param accountId 계정 식별자
     * @return 조회 결과
     */
    Optional<Account> findById(Long accountId);

    /**
     * 전체을 조회한다
     *
     * @return 조회 결과 목록
     */
    List<Account> findAll();

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

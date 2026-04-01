package com.house.biet.user.query;

/**
 * 사용자 조회 전용 서비스
 *
 * <p>
 * - User 도메인에 대한 **조회(Query) 책임만**을 가지는 서비스
 * - Command(Service) 영역에서 직접 Repository에 접근하지 않기 위한 중간 계층
 * - accountId → userId 변환과 같은 조회 전용 로직을 캡슐화
 * </p>
 *
 * <p>
 * 사용 목적
 * <ul>
 *   <li>Order, Payment 등 다른 도메인에서 User 엔티티 전체를 의존하지 않도록 분리</li>
 *   <li>조회 전용 로직을 Command 영역과 명확히 분리 (CQRS 성격)</li>
 *   <li>accountId 기반 인증 구조에서 userId를 안전하게 획득</li>
 * </ul>
 * </p>
 */
public interface UserQueryService {

    /**
     * accountId를 기반으로 userId 조회
     *
     * @param accountId 인증 계정 ID
     * @return 조회된 userId accountId에 해당하는 User가 존재하지 않을 경우
     */
    Long getUserIdByAccountId(Long accountId);
}

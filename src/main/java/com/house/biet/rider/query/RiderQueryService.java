package com.house.biet.rider.query;

/**
 * 라이더 조회 전용 Query Service
 *
 * <p>
 * - Rider Aggregate를 직접 반환하지 않고, 필요한 조회 정보만 제공한다
 * - Command 영역과 분리된 읽기 전용 서비스
 * - 외부 식별자(Nickname)를 내부 식별자(riderId)로 변환하는 책임을 가진다
 * </p>
 *
 * <p>
 * ❗ 설계 의도
 * <ul>
 *   <li>Order, Delivery 등 다른 도메인이 Rider Aggregate에 직접 의존하지 않도록 차단</li>
 *   <li>외부 입력값(String)을 그대로 받아 내부 VO(Nickname) 생성 책임을 Query 영역에 둠</li>
 *   <li>ID 조회 목적이므로 Entity 전체 조회를 피하고 성능을 고려</li>
 * </ul>
 * </p>
 */
public interface RiderQueryService {

    /**
     * 라이더 닉네임으로 라이더 ID 조회
     *
     * @param nicknameValue 라이더 닉네임 문자열 (외부 입력값)
     * @return 조회된 라이더 ID
     */
    Long getRiderIdByNickname(String nicknameValue);

    /**
     * Account ID로 라이더 ID 조회
     *
     * @param accountId 라이더와 연결된 계정 ID
     * @return 조회된 라이더 ID
     */
    Long getRiderIdByAccountId(Long accountId);
}

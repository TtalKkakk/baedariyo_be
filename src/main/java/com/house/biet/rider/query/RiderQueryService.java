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
     * <p>
     * - API / Controller 계층에서는 String 형태의 닉네임만 전달
     * - Query Service 내부에서 Nickname VO로 변환하여 조회
     * - 라이더가 존재하지 않으면 예외를 발생시킨다
     * </p>
     *
     * @param nicknameValue 라이더 닉네임 문자열 (외부 입력값)
     * @return 조회된 라이더 ID
     * @throws com.house.biet.global.response.CustomException 라이더가 존재하지 않을 경우
     */
    Long getRiderIdByNickname(String nicknameValue);

    /**
     * Account ID로 라이더 ID 조회
     *
     * <p>
     * - 라이더가 연관된 계정(Account)을 기준으로 내부 식별자인 Rider ID를 반환
     * - Command 도메인에서는 Account ID만 알고 있어도 라이더를 참조할 수 있게 해줌
     * - 라이더가 존재하지 않으면 {@link com.house.biet.global.response.CustomException}을 발생시킴
     * </p>
     *
     * <p>
     * 사용 예시:
     * <pre>
     * Long riderId = riderQueryService.getRiderIdByAccountId(accountId);
     * </pre>
     * </p>
     *
     * @param accountId 라이더와 연결된 계정 ID
     * @return 조회된 라이더 ID
     * @throws com.house.biet.global.response.CustomException 라이더가 존재하지 않을 경우
     */
    Long getRiderIdByAccountId(Long accountId);
}

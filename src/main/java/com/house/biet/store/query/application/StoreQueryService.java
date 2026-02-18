package com.house.biet.store.query.application;

import com.house.biet.store.command.domain.entity.Menu;

import java.util.List;
import java.util.UUID;

/**
 * Store Aggregate에 대한
 * <b>조회 전용(Query) 서비스</b>입니다.
 *
 * <p>
 * 본 서비스는 Store에 속한 {@link Menu} 목록을 조회하는
 * <b>읽기 전용 유스케이스</b>를 제공합니다.
 * </p>
 *
 * <p>
 * Command(Service)와 분리된 CQRS 구조를 따르며,
 * 데이터 변경이나 비즈니스 정책 판단을 수행하지 않습니다.
 * </p>
 *
 * <p>
 * 트랜잭션은 구현체에서 {@code readOnly = true}로 관리되며,
 * 조회 성능 최적화, 캐시 적용, 권한 검증 등의 확장 포인트로 사용됩니다.
 * </p>
 *
 * <p><b>주의사항</b></p>
 * <ul>
 *   <li>본 서비스는 조회 전용이므로 상태 변경 로직을 포함하지 않습니다.</li>
 *   <li>{@link Menu}는 Store Aggregate 내부 엔티티이므로,
 *       Store 식별자를 기준으로만 조회해야 합니다.</li>
 * </ul>
 */
public interface StoreQueryService {

    /**
     * 내부 Store 식별자(ID)를 기준으로
     * 해당 Store에 속한 모든 Menu를 조회합니다.
     *
     * @param storeId Store의 내부 식별자
     * @return Store에 속한 Menu 목록
     */
    List<Menu> getMenusByStoreId(Long storeId);

    /**
     * 외부에 노출되는 Store 공개 식별자(publicId)를 기준으로
     * 해당 Store에 속한 모든 Menu를 조회합니다.
     *
     * @param publicStoreId Store의 외부 공개 식별자(UUID)
     * @return Store에 속한 Menu 목록
     */
    List<Menu> getMenusByPublicId(UUID publicStoreId);
}

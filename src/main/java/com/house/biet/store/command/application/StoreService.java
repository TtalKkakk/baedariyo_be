package com.house.biet.store.command.application;

import com.house.biet.global.response.CustomException;
import com.house.biet.global.response.ErrorCode;
import com.house.biet.store.command.StoreRepository;
import com.house.biet.store.command.domain.aggregate.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Store Aggregate에 대한
 * <b>단순 CRUD 접근을 담당하는 서비스</b>입니다.
 *
 * <p>
 * 이 서비스는 비즈니스 규칙이나 정책 판단을 포함하지 않으며,
 * {@link Store} 엔티티의 조회·저장과 같은
 * <b>도메인 접근을 위한 얇은 래퍼 역할</b>만 수행합니다.
 * </p>
 *
 * <p>
 * 실제 유스케이스나 정책 로직은
 * 별도의 Command / Facade 서비스에서 처리하고,
 * 본 서비스는 트랜잭션 경계 및 Repository 접근만 담당합니다.
 * </p>
 *
 * <p><b>주의사항</b></p>
 * <ul>
 *   <li>본 서비스에 비즈니스 로직을 추가하지 않습니다.</li>
 *   <li>엔티티의 상태 변경은 {@link Store} 내부 메서드를 통해 수행해야 합니다.</li>
 * </ul>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class StoreService {

    private final StoreRepository storeRepository;

    /**
     * Store 엔티티를 저장합니다.
     *
     * @param store 저장할 Store 엔티티
     * @return 저장된 Store 엔티티
     */
    public Store save(Store store) {
        return storeRepository.save(store);
    }

    /**
     * 내부 식별자(ID)를 기준으로 Store를 조회합니다.
     *
     * @param storeId Store의 내부 ID
     * @return 조회된 Store
     * @throws CustomException Store가 존재하지 않을 경우
     */
    @Transactional(readOnly = true)
    public Store getStoreById(Long storeId) {
        return storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }

    /**
     * 외부 공개용 식별자(publicId)를 기준으로 Store를 조회합니다.
     *
     * @param publicStoreId 외부에 노출되는 Store UUID
     * @return 조회된 Store
     * @throws CustomException Store가 존재하지 않을 경우
     */
    @Transactional(readOnly = true)
    public Store getStoreByPublicId(UUID publicStoreId) {
        return storeRepository.findByPublicId(publicStoreId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));
    }
}


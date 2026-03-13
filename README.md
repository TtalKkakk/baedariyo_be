# baedariyo_be

배달이요 서비스의 메인 백엔드 서버입니다.

## Tech Stack

- Java 17
- Spring Boot 4.0.2
- Spring Security + JWT
- Spring Data JPA
- Gradle

## Quick Start

### 1) 필수 설정 파일 생성

`src/main/resources/application-secret.yml` 파일을 생성하고 JWT 값을 넣어주세요.

```yaml
jwt:
  secret-key: change-this-to-a-long-secret-key
  access-token-validity-seconds: 3600
  refresh-token-validity-seconds: 1209600
```

`application.yml`에서 위 파일을 `optional:application-secret.yml`로 import 하고 있으며, JWT 값이 없으면 서버가 정상 부팅되지 않습니다.

### 2) 서버 실행

```bash
./gradlew bootRun
```

기본 포트는 `8080`입니다.

### 3) 테스트 실행

```bash
./gradlew test
```

## 인증 규칙

- `POST /api/auth/**` : 인증 없이 호출 가능
- 그 외 `/api/**` : `Authorization: Bearer <accessToken>` 필요

## 응답 포맷

대부분 API는 아래 형태의 공통 응답을 사용합니다.

```json
{
  "code": 20000,
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {}
}
```

## 주요 API (프론트 연동 기준)

### Auth

- `POST /api/auth/user/signup`
- `POST /api/auth/user/login`
- `PATCH /api/auth/user/withdraw`
- `POST /api/auth/rider/signup`
- `POST /api/auth/rider/login`
- `PATCH /api/auth/rider/withdraw`

### Store

- `POST /api/stores`
- `GET /api/stores/{storePublicId}`
- `GET /api/stores/{storePublicId}/menus`

### Review

- `POST /api/stores/{storePublicId}/reviews`
- `GET /api/stores/{storePublicId}/reviews`
- `GET /api/reviews/{storeReviewPublicId}`
- `DELETE /api/reviews/{storeReviewPublicId}`
- `GET /api/reviews/me`

### Order

- `POST /api/orders/rider/assign` (사용자 주문 생성)
- `POST /api/orders/users/create` (라이더 배정)

### Payment

- `POST /api/payments`
- `POST /api/payments/{paymentId}/approve`
- `POST /api/payments/{paymentId}/fail`
- `POST /api/payments/{paymentId}/cancel`
- `GET /api/payments/{paymentId}`
- `GET /api/payments/my?status=READY|REQUESTED|APPROVED|FAILED|CANCELED`

## 프론트 연동

프론트(`baedariyo_fe`)에서는 기본적으로 `http://localhost:8080`을 API 서버로 사용합니다.

```env
VITE_API_BASE_URL=http://localhost:8080
```

## Troubleshooting

- `Could not resolve placeholder ... jwt...` : `application-secret.yml` 누락
- `401 Unauthorized` : 로그인 토큰 미전달 또는 만료
- `Port 8080 already in use` : 기존 프로세스 종료 후 재실행

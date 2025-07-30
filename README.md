# Finbuddy: 자산 관리 플랫폼

### 프로젝트 기간
2025.02.10 ~ 2025.03.12

### 프로젝트 개요
모든 계좌를 한 곳에서 조회하여 효율적으로 지출을 관리하고, 송금 및 자동 이체 등 안전한 금융 서비스를 제공합니다.

## 주요 기능
- 마이데이터 연동 (더미데이터 생성)
- 거래 내역 분석 및 시각화
- 이체 및 자동 이체
- 예산 관리 및 알림
- 금융 상품 추천 및 조회

<br>

**회원가입 및 로그인**
- JWT 기반 로그인 및 인증
  - Access Token + HttpOnly Refresh Token 방식
  - Spring Security 기반 인증 필터 체인
- SMTP 이메일 기반 회원 가입

| 회원가입 | 로그인 |
|--|--|
| <img src="https://github.com/user-attachments/assets/736af21a-abed-4e94-84e2-d99468f7e811"/> | <img src="https://github.com/user-attachments/assets/285f6754-cdb1-4c33-b4ef-9a194d754f54"/>  |

<br/>

**마이데이터 연동 약관 동의**
- 더미 데이터를 생성하여 사용자의 계좌 및 거래 내역 자동 연동

| 마이데이터 연동 약관 동의 |
|--|
| <img width="400" src="https://github.com/user-attachments/assets/eb3dd398-7dc7-40a7-84b5-c0ea9d5a31a5" /> |

<br/>

**거래 내역 분석 및 시각화**
- 사용자의 지출 및 수입 내역을 분류 및 통계화하여 시각화
- 월별, 카테고리별 지출 분석 기능 제공 
- React 기반 차트 라이브러리를 활용하여 동적 시각화 및 사용자 친화적인 UI 제공 
- 예산 대비 지출 분석 제공, 실시간 데이터 반영으로 가시적인 소비 관리 가능
  
| 거래 내역 분석 및 시각화 |  |
|--|--|
| <img src="https://github.com/user-attachments/assets/07c54473-4bb7-49e8-afc3-14ef9bf1cd1f"/> | <img  src="https://github.com/user-attachments/assets/395ac1af-68de-4223-9d27-933ecba8f3fc"/> | 
| <img src="https://github.com/user-attachments/assets/a53302c7-1ea5-46ef-bbd7-2a1b6e96258d"/> | <img src="https://github.com/user-attachments/assets/e8ecde0e-3b1c-488d-a35a-aead2a8980dd"/> |

<br/>

**금융 상품 추천 및 조회**
- 금융감독원 API를 이용한 예/적금 상품 조회

| 금융 상품 추천 및 조회 |  |
|--|--|
| <img src="https://github.com/user-attachments/assets/47b2512f-feb2-427d-8183-d32c5f231710"/> | <img src="https://github.com/user-attachments/assets/0e89e75f-09ac-4156-be7f-25634a8ee798"/>  |

<br/>

**송금 기능 (OTP 설정 → 이체 정보 입력 → 비밀번호 입력)**
1. OTP 인증 설정 확인 - 사용자가 OTP 인증(Google Authenticator)을 등록했는지 확인
    - 미등록 시, OTP 설정 안내 페이지로 이동하여 OTP 등록 및 검증 과정 진행 
2. 이체 정보 입력 - 이체할 수취인 계좌번호, 이체 금액, 이체 메모 등의 정보 입력
    - 입력된 정보에 대해 프론트엔드 수준에서 유효성 검사 및 백엔드 검증 로직 수행 
3. 이체 비밀번호 입력
    - 이체 실행 전, 사용자가 등록한 이체용 비밀번호 입력
    - 비밀번호는 단방향 해시(Bcrypt) 처리되어 저장되며, 해시 비교 방식으로 검증 
4. OTP 인증 검증
    - Google Authenticator에서 생성된 OTP 코드 입력
    - 서버 측에서 TOTP 알고리즘 기반 OTP 검증 수행 
5. 이체 실행
    - 입력된 이체 정보를 바탕으로 내부 계좌 간 잔액 차감 및 이체 처리 수행
    - 이체 성공 시 거래 내역 기록

| 송금 기능 |  |
|--| --|
| <img src="https://github.com/user-attachments/assets/8757df7b-6c33-48c9-b39b-2c233476cb10"/> | <img src="https://github.com/user-attachments/assets/a31e521f-ea7f-4f8d-9825-dccb41296881"/> | 
| <img src="https://github.com/user-attachments/assets/c47b226e-2a57-4892-8fb8-eedfb6b64c47"/> | <img src="https://github.com/user-attachments/assets/81ffa202-03dc-4643-98ae-b08e71f57b4a"/> |

<br/>

**예산 관리**
- 사용자별 월간 예산 설정 기능 제공
- 설정된 예산 초과 지출 시 알림 기능 구현
  - SSE(Server-Sent Events) 기반 실시간 예산 초과 알림 제공

| 예산 관리 |
|--|
| <img width="400" src="https://github.com/user-attachments/assets/ad2d346b-6d64-4079-a724-6c992dd24e11"/> &nbsp; <img width="200" src="https://github.com/user-attachments/assets/00dfad89-ea37-4006-be9c-83f049721b0c"/> | 

<br/>

**자동 이체 관리**
- 정기 자동 이체 등록 및 실행 기능 구현
- 설정 주기에 따른 자동 이체 실행
  - 등록된 자동이체에 대해 Spring Batch를 활용한 배치 처리
  - 자동 이체 성공 및 실패시 알림 기능
- 자동 이체 내역 확인 및 수정/삭제 기능 제공

| 자동 이체 관리 |
|--|
| <img width="400" src="https://github.com/user-attachments/assets/a3cbde25-ce7f-4eae-bba7-5d0c95bb534e"/> &nbsp; <img width="98" src="https://github.com/user-attachments/assets/165e3687-7f6e-4760-8aa7-c8cc9825c3e2"/> &nbsp; <img width="118" src="https://github.com/user-attachments/assets/cd8d4eee-7987-40fd-8a38-76ba80bbf7d3"/> | 

<br>

## Tech Stack

| 구분             | 사용 기술                                         |
|--|--|
| **Frontend**  | React, JavaScript, Vite |
| **Backend**   | Java, Spring, Spring Boot, Spring Batch, JWT |
| **Infra**   | Git Actions, Docker, Nginx, AWS RDS(MySQL), Route 53, ACM, EC2, S3 |
| **Tools** | Figma, Notion, Github, Vscode, IntelliJ, Swagger |

<br>

## Infra
![image](https://github.com/user-attachments/assets/bb9aea8f-74e6-40c8-bf3d-cc1236aa6474)

<br>

## ERD
![FinBuddy](https://github.com/user-attachments/assets/bc5af814-a07a-4432-8361-e14e41691d45)

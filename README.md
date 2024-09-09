![header](https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2F5e6bc2a9-57b4-418c-8c61-4488307bc0af%2Fheader_logo.png?table=block&id=4ec2a845-9fa5-49c5-972b-9675a3387e43&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=1460&userId=1915f230-f6d8-4fc2-88df-57efadf78940&cache=v2)

<div id="site">

## 1인가구를 위한 홈쇼핑 🛒
```
💡 나 혼자 살고(Live) 나 혼자 산다(Buy)! 자취생들을 위한 홈쇼핑
나 혼자 산다는 1인가구를 위한 꿀템을 판매하고 구매할 수 있는 라이브커머스 사이트입니다.
```

# [🏠 나혼자산다 사이트 바로가기 🏡](https://livealone.shop/)

## GitHub
| front                                                              | back                                                                                                      |
|--------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------|
| [![github](https://img.icons8.com/?size=100&id=ARy6tFUfwclb&format=png&color=000000)](https://github.com/UDM2C/frontlivealone) | [![github](https://img.icons8.com/?size=100&id=ARy6tFUfwclb&format=png&color=000000)](https://github.com/UDM2C/UDM2C) |


</div>

## 목차
- [👨🏻‍👩🏻‍👧🏻‍👦🏻 UDM2C 팀 소개](#-udm2c-팀-소개)
- [🎨 Teck Stack](#-teck-stack)
- [🌌 환경변수](#-환경변수)
- [🎯 서비스 가이드](#-서비스-가이드)
- [📑 Technical Documentation](#-technical-documentation)
  - [🔱 Branch Rule](#-branch-rule)
  - [🌠 Commit Rule](#-commit-rule)
- [🗣️기술적 의사결정](#-기술적-의사결정)
- [⚠️ 트러블 슈팅](#-트러블-슈팅)

<div id="team">

# 👨🏻‍👩🏻‍👧🏻‍👦🏻 UDM2C 팀 소개

   
| 유규리(리더)                     | 권수연                         | 김채민                         | 김혜은                         | 노석준                         |
|-------------------------------|-------------------------------|-------------------------------|-------------------------------|-------------------------------|
| [![유규리](https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2F7c2b9490-1e23-4f57-acd3-6d223fcf54ac%2F1.png?table=block&id=4e04a7a6-6479-4451-a8d3-a2f1b8eccef5&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=240&userId=1915f230-f6d8-4fc2-88df-57efadf78940&cache=v2)](https://github.com/yuguri76) | [![권수연](https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2F2333fc0a-19f7-4641-91cf-7309c8b33316%2F3.png?table=block&id=fa26d3c2-3864-4fba-9826-08eafa18504f&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=260&userId=1915f230-f6d8-4fc2-88df-57efadf78940&cache=v2)](https://github.com/ggumi030) | [![김채민](https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2F81729450-ab76-4a3b-896d-c321fe2d840d%2F2.png?table=block&id=40f89b24-c0f9-4437-833e-367419c33117&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=250&userId=1915f230-f6d8-4fc2-88df-57efadf78940&cache=v2)](https://github.com/chamcham0707) | [![김혜은](https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2Fb575d21b-5aee-4987-8165-c10d3a7de9e0%2F5.png?table=block&id=3234afe4-76c7-46c9-8a50-d0ff70fbbfba&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=250&userId=1915f230-f6d8-4fc2-88df-57efadf78940&cache=v2)](https://github.com/MetroDefro) | [![노석준](https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2F7798172f-fc39-44fd-824e-2de55c1ed04a%2F4.png?table=block&id=270d79a4-d36a-4c07-be21-4e7a288b8c86&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=250&userId=1915f230-f6d8-4fc2-88df-57efadf78940&cache=v2)](https://github.com/kopite97) |

<details>
<summary>유규리(리더)</summary>
<div markdown="1">

- 카카오페이 결제 시스템 구현
- 토스페이 결제 시스템 구현
- 주문 내역(결제 내역) 조회
- 메인 페이지
- 스트리밍 가이드 페이지
- 푸터 구현
- 결제 페이지
- 마이페이지(주문 내역)
- 주문 내역 상세페이지
- 결제완료 페이지
- 이외 스트리밍 페이지 등 css 수정


</div>
</details>

<details>
<summary>권수연</summary>
<div markdown="1">

- 실시간 스트리밍 구현
- 유저 정보 조회
- 유저 정보 수정
- 방송 코드 입력
- 주문 생성 및 재고 차감
- 재고 확인 및 관리
- 동시성 제어
- 알림 기능
- 로그인 페이지
- 스트리밍 페이지
- AWS Application Load Balancer 적용
- https 적용
- 배송지 입력 시 도로명 주소 API 적용
- 결제 페이지에서 timeout을 적용하여 10분 안에 결제가 이루어지도록 제한

</div>
</details>

<details>
<summary>김채민</summary>
<div markdown="1">

- 실시간 스트리밍 구현
- 현재 방송 조회
- 상품정보조회
- 상품정보등록
- 관리자 기능 구현
- 캐싱 적용
- 알림 기능

</div>
</details>

<details>
<summary>김혜은</summary>
<div markdown="1">

- 실시간 스트리밍 구현
- 유저별 방송 내역 조회
- 유저별 배송 정보 조회
- 예약 기능
- 알림 기능
- 스트리머 페이지
- 예약 페이지
- nginx 프록시
- axiosInstance 구성, JWT 예외처리

</div>
</details>

<details>
<summary>노석준</summary>
<div markdown="1">

- 실시간 채팅 구현

</div>
</details>
</div>

<div id="teck-stack">
  
# 🎨 Teck Stack

| Type           | Tech                                                                                                                                                                                                                                                                                                                                                                                                                                     | 
|----------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| IDE            | ![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)                                                                                                                                                                                                                                                                                                            |
| Framework      | ![Spring](https://img.shields.io/badge/SpringBoot_3.3.2-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)                                                                                                                                                                                                                                                                                                                   |
| Language       | ![Java](https://img.shields.io/badge/java_JDK17-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)                                                                                                                                                                                                                                                                                                                          |
| Database       | ![MySQL](https://img.shields.io/badge/mysql_8.0.28-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white) ![MongoDB](https://img.shields.io/badge/mongoDB_3.8-47A248?style=for-the-badge&logo=mongodb&logoColor=white)                                                                                                                                                                                                               |
| Cashing        | ![Redis](https://img.shields.io/badge/redis_7.2.5-FF4438?style=for-the-badge&logo=redis&logoColor=white)                                                                                                                                                                                                                                                                                                                                 |
| Message Broker | ![Kafka](https://img.shields.io/badge/kafka-231F20?style=for-the-badge&logo=apachekafka&logoColor=white)                                                                                                                                                                                                                                                                                                                                 |
| Live Streaming | ![Hls](https://img.shields.io/badge/hls-000000?style=for-the-badge&logo=hls&logoColor=white)  ![RTMP](https://img.shields.io/badge/rtmp-d2f3f8?style=for-the-badge&logo=rtmp&logoColor=white)                                                                                                                                                                                                                                            |
| Live Chatting  | ![Websocket](https://img.shields.io/badge/websocket-000000?style=for-the-badge&logo=websocket&logoColor=white)  ![Stomp](https://img.shields.io/badge/stomp-000000?style=for-the-badge&logo=stomp&logoColor=white)                                                                                                                                                                                                                       |
| Tools          | ![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white) ![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white) ![Docker](https://img.shields.io/badge/docker_6.0.16-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)                                                                                                     |
| Collaboration  | ![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white) ![Figma](https://img.shields.io/badge/figma-%23F24E1E.svg?style=for-the-badge&logo=figma&logoColor=white) ![Slack](https://img.shields.io/badge/slack-4A154B?style=for-the-badge&logo=slack&logoColor=white)                                                                                                                |
| Code Editor    | ![Visual Studio Code](https://img.shields.io/badge/Visual%20Studio%20Code-0078d7.svg?style=for-the-badge&logo=visual-studio-code&logoColor=white)                                                                                                                                                                                                                                                                                        |
| FrontEnd       | ![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB) ![HTML5](https://img.shields.io/badge/html5-E34F26?style=for-the-badge&logo=html5&logoColor=white) ![css](https://img.shields.io/badge/css3-1572B6?style=for-the-badge&logo=css3&logoColor=white) ![Javascript](https://img.shields.io/badge/javascript-F7DF1E?style=for-the-badge&logo=javascript&logoColor=white)        |
| Infra          | ![EC2](https://img.shields.io/badge/EC2-FF9900?style=for-the-badge&logo=amazonec2&logoColor=white) ![ELB](https://img.shields.io/badge/ELB-8C4FFF?style=for-the-badge&logo=awselasticloadbalancing&logoColor=white) ![Route53](https://img.shields.io/badge/Route53-8C4FFF?style=for-the-badge&logo=amazonroute53&logoColor=white) ![Route53](https://img.shields.io/badge/nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)  |

</div>

<div id = "env">

# 🌌 환경변수
```
MYSQL_USER={user}
MYSQL_PASSWORD={password}
JWT_SECRET_KEY={secret_key}
ACCESS_EXPIRE_TIME=600000
REFRESH_EXPIRE_TIME=3600
SOCIAL_GOOGLE_CLIENT_ID={client_id}
SOCIAL_GOOGLE_CLIENT_SECRET={secret_key}
SOCIAL_NAVER_CLIENT_ID={client_id}
SOCIAL_NAVER_CLIENT_SECRET={secret_key}
SOCIAL_KAKAO_CLIENT_ID={client_id}
SOCIAL_KAKAO_CLIENT_SECRET={secret_key}
MONGO_INITDB_ROOT_USERNAME={user}
MONGO_INITDB_ROOT_PASSWORD={password}
DATABASE_HOST={host}
SERVER_HOST={host}
FRONT_SERVER_HOST={host}
KAFKA_HOST={host}
KAKAO_CID={client_id}
KAKAO_SECRET_KEY={secret_key}
TOSS_CLIENT_KEY={client_id}
TOSS_SECRET_KEY={secret_key}
ADMIN_CODE={admin_code}
DEFAULT_STREAM_KEY={stream_key}
LOGGING_LEVEL=INFO
ADMIN_TOKEN={admin_token}
MAIL_USERNAME={user}
MAIL_PASSWORD={password}
MAIL_TIMEOUT=5000
PROTOCOL={protocol}
KAFKA_CHAT_GROUP={group}
```

<div id = "feature">
  
# 🎯 서비스 가이드
#### 저희 프로젝트는 스트리머 입장에서 방송을 할 수도 있고, 시청자 입장에서 라이브 중인 방송을 시청할 수도 있습니다.

<details>
<summary>🎥 스트리머 (방송 하기)</summary>
<div markdown="1">

![image](https://github.com/user-attachments/assets/e41db36e-ca25-4fb0-a182-ff364efbbfea)

1. **방송 예약 서비스**
- 방송 예약 서비스에서 내가 방송하고 싶은 날짜와 시간을 선택하자!
- Email로 **스트림 키**(방송할 때 쓰는 키)를 받을 수 있어요!

2. **방송 시작 서비스**
- 방송을 예약한 날짜와 시간이 되었다면?
- 방송 시작 서비스에서 스트리밍을 시작해봐요!

</div>
</details>

<details>
<summary>👥 시청자 (방송 보기)</summary>
<div markdown="1">

![image](https://github.com/user-attachments/assets/9d8508a1-f98c-4084-867f-d0d770269232)

1. **실시간 방송 보러 가기**
- 현재 라이브 중인 방송이 있나요?
- 그렇다면 **보러 가기** 버튼을 클릭해 실시간 방송을 시청해봐요!
- 이 페이지에서는 다른 시청자들과 실시간 채팅이 가능해요!
- 방송 중인 제품이 마음에 든다면 **구매**도 해볼까요?


- 채팅 닉네임 색상 - 관리자 권한을 가진 사람들은 빨간색, 일반 유저들은 빨간색을 제외한 다른 색상이 랜덤으로 설정됩니다!
- 방송이 시작할 때, 재고가 10개 이하로 남았을 때, 매진 됐을 때 모든 사용자에게 알림 메세지가 뜨게 됩니다 !


</div>
</details>

<details>
<summary>💵 구매하기 (실제 돈이 차감되지 않으니 안심하세요 ! 가상 머니로 결제가 진행됩니다.)</summary>
<div markdown="1">

![image](https://github.com/user-attachments/assets/3be6c9e5-4ba4-41dc-bba4-92c9eb613537)
1. **상품 구매 페이지로**
- 방송 아래의 구매 하기 버튼을 누르면 상품을 구매할 수 있어요!

**Step 1: 주문 수량 입력하기**

![image](https://github.com/user-attachments/assets/9b3cc36d-4bc6-4738-944b-a6f86430298d)

- 수량을 입력하고 **완료 버튼** 꾸~욱!
- 10분 안에 결제를 완료 해야 돼요!

**Step 2: 배송 정보 입력하기**

![image](https://github.com/user-attachments/assets/c5c3e4f3-e424-487f-866d-3d09acaa5279)

- 마이페이지에 설정한 주소를 가져오거나 새 주소를 입력할 수 있어요!
- **카카오 우편번호 찾기 서비스**를 통해 편하게 주소를 입력할 수 있어요!

**Step 3: 결제**

![image](https://github.com/user-attachments/assets/6afd38cc-253e-4d16-b497-c06276f8e8c9)

- 카카오 페이, 토스 페이 중 선택 가능!
- 실제 돈이 빠져나가지 않으니 안심하세요!

</div>
</details>


<div id = "tech-doc">
  
# 📑 Technical Documentation

<details>
<summary>⭐와이어 프레임</summary>
<div markdown="1">

## 메인페이지

![image](https://github.com/user-attachments/assets/76465c65-6204-473d-a1bf-1da46435b391)

## 스트리밍 페이지

![image](https://github.com/user-attachments/assets/dca128a8-dd33-4eb1-90ce-03a97cbe72dc)

## 스트리머 페이지

![image](https://github.com/user-attachments/assets/f5772f94-52e6-4555-acf7-1cb7c65b4e44)

## 로그인 페이지

![image](https://github.com/user-attachments/assets/5ebdbe7f-e92e-4558-80be-501a1ca97f44)

## 방송 예약 페이지

![image](https://github.com/user-attachments/assets/f7e377a6-2f03-49b4-af5f-bb3e2224c0be)

## 마이 페이지

![image](https://github.com/user-attachments/assets/edb56263-d004-4cb8-bf53-4d5934bb5062)

## 관리자 페이지

![image](https://github.com/user-attachments/assets/316c4179-6bce-4882-a4f0-daffdc17fad8)

## 결제 페이지

![image](https://github.com/user-attachments/assets/d1cfaefb-7e1e-4dbe-a4bb-a0e1aba120d1)

## 결제 완료 페이지

![image](https://github.com/user-attachments/assets/ffd3a04b-a193-4fef-9ff2-8c44eb640c72)

## 에러 페이지

![image](https://github.com/user-attachments/assets/89e8e589-cde1-4d57-9aa7-371472de832d)

</div>
</details>

<details>
<summary>🧬 ERD DIAGRAM</summary>
<div markdown="1">
 
   ![ERD](https://www.notion.so/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2Ff8dacd42-637d-44d6-8051-ef9bb30e4690%2Fimage.png?table=block&id=524b6ec1-df77-471b-b3de-54670e3049da&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=2000&userId=1915f230-f6d8-4fc2-88df-57efadf78940&cache=v2)

</div>
</details>

<details>
<summary> 🔨 API 명세서</summary>
<div markdown="1">

## [🔨 API 명세서](https://teamsparta.notion.site/f2dbee8978734924825667a9dca9367c?v=83ccba3cf7d646a89cdfea9e1c212830&pvs=4)

</div>
</details>

<details>
<summary> 💻 아키텍처</summary>
<div markdown="1">

## 💻 아키텍처
 ![아키텍처](https://teamsparta.notion.site/image/https%3A%2F%2Fprod-files-secure.s3.us-west-2.amazonaws.com%2F83c75a39-3aba-4ba4-a792-7aefe4b07895%2F520f9dd6-ccd3-4bef-a716-1cf16ed6f187%2Fimage.png?table=block&id=1d58fd07-f9c6-4383-95df-9574d3657b80&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&width=2000&userId=&cache=v2)

</div>
</details>

<details>
<summary>🔱 Branch Rule</summary>
<div markdown="1">
  
## 🔱  Branch Rule
1. 이슈 만들기(git issue) -> [기능] 설명
2. 관련 브랜치 만들기 ex) feature/#번호 - 설명
3. 커밋메시지 : 기능 : 설명 ex) feat : 설명
4. 완료 후 pull request 올리기
2명 이상 approve 눌러야 merge 되도록 설정.
(반드시 풀리퀘 작성자 외 다른 사람이 merge 눌러주기!)
1. pull request에 각자 리뷰하나씩 꼭 달기


병합 순서
- 기능 단위 별 브랜치 — 병합 → 디벨롭 브랜치-최종병합→ 마스터 브랜치

</div>
</details>


<details>
<summary>🌠 Commit Rule</summary>
<div markdown="1">
  
## 🌠 Commit Rule

| 작업 타입 | 작업내용 |
| --- | --- |
| ✨ feature | 새로운 기능을 추가 |
| 🐛 bugfix | 버그 수정 |
| ♻️ refactor | 코드 리팩토링 |
| 🩹 fix | 코드 수정 |
| 🚚 move | 파일 옮김/정리 |
| 🔥 del | 기능/파일을 삭제 |
| 💄 style | css |
| 🍻 test | 테스트 코드를 작성 |
| 🎨 readme | readme 수정 |
| 🙈 gitfix | gitignore 수정 |
| 🔨script | package.json 변경(npm 설치 등) |


</div>
</details>



<details>
<summary>🚀 Code Convention</summary>
<div markdown="1">

## 🚀 Code Convention

- 구글 코드 컨벤션 적용 - 코드스타일 intelliJ에 적용
- JWT 토큰 전달 방식 - 리프레시 토큰은 `쿠키`, 엑세스 토큰은 `헤더`로 전달
- 인스턴스 생성 방식 - 빌더 패턴으로 통일
- 클래스는 파스칼 표기법 사용
- 함수명, 변수명에는 카멜 표기법 사용
- 약어 쓰지 않는다.
- Bean 주입 방식 - require 사용
- 주석 - 자바 독스 주석(기능, 클래스에 대한 것. 단 기본 CRUD는 제외)
- dto - getter, builder만 사용
- mapper 사용: static 메서드
-  상수 네이밍 규칙 - 전체 대문자, 단어연결(_) 사용, static final 사용
- 예외 메세지 Properties 사용
src>resources>messages.properties
- CommonResponseDto 생성은 컨트롤러에서 하기

</div>
</details>

<div id ="decision">

# 🗣️ 기술적 의사결정
<details>
<summary>🧩 Kafka</summary>
<div markdown="1">

기획단계에서 스케일 아웃을 고려하며 메시지 스트리밍 기능을 사용하기 위해 kafka와 RabbitMQ,Redis Queue,SQS와 같은 외부 메시지 브로커를 이용하기로 했습니다.

그중 Kafka가 가장 높은 처리량과 확장성을 제공할 수 있었고, 데이터 스트리밍과 로그 수집에 적합하다고 판단이 되었습니다. 최근 기업에서 가장 널리 사용되고 있는 점 또한 선정의 이유가 되었습니다.

</div>
</details>

<details>
<summary>🧩 Redis: 캐싱, 동시성 제어</summary>
<div markdown="1">

**Redis**

Redis는 In Memory 데이터베이스이기 때문에 응답 속도가 빠릅니다. 그리고 Redis는 list, set, hash 등 다양한 데이터 구조를 지원합니다. 또한, Redis는 설정과 사용이 비교적 간다하고, 다양한 프로그래밍 언어와의 호환성을 제공하여, Redis를 쉽게 도입할 수 있습니다. 이러한 특징들을 고려하여 캐시로 Redis를 선택하였습니다.

**동시성 제어**

재고 관리 및 주문 처리에 동시성 제어를 하기위해 낙관적 락, 비관적 락, 분산 락 등 다양한 방법에 대해 공부했고, 프로젝트에 가장 적합한 방식이 무엇일지 고민했습니다. 저희 프로젝트에서는 데이터 캐싱을 위해 이미 Redis를 사용하고 있었기에 분산 락을 적용할 때 따로 세팅이 필요가 없었고, 직접 DB에 락을 거는 낙관적 락이나 비관적 락과는 달리 분산 락을 직접 DB에 락을 거는 방식이 아니라 DB에 부하가 적다는 점도 고려하여 단일 스레드인 Redis를 활용한 분산 락을 통해 동시성 제어를 하기로 결정했습니다.

</div>
</details>

<details>
<summary>🧩 Websocket, Stomp: 실시간 채팅</summary>
<div markdown="1">

HTTP로 실시간채팅을 구현할 수 있으나, 요청과 응답후 연결이 끊어지기 때문에 지속적인 재연결이 필요하고 HTTP통신은 매번 요청을 보낼 때마다 헤더와 기타 메타데이터를 포함해 전송해야하므로 오버헤드가 발생할 수 있습니다.  웹 소켓은 Stateful한 특성을 가지고 있어 오버헤드가 적고, 양방향 통신을 지원하므로 서버와 클라이언트가 자유롭게 데이터를 주고 받을 수 있습니다. 초기 개발 단계 에서는 웹 소켓 만을 사용하여 실시간 채팅을 구현하였으나, 더 높은 확장성을 위해 하위 프로토콜인 STOMP를 적용하였습니다.

</div>
</details>

<details>
<summary>🧩 RTMP, HLS: 실시간 스트리밍</summary>
<div markdown="1">

여러 프로토콜을 찾아 보았으나 각각의 장 단점이 있었습니다.

HLS는 제일 대중적이나 다른 스트리밍 프로토콜에 비해 상대적으로 지연 시간이 높았고, RTMP는 flash 지원이 끊겨 HTML5에서는 플레이 하지 못해 별도의 인코딩 과정이 필요했습니다. WebRTC는 개발 관련 자료가 많았으나 1대1 연결, 소규모 그룹 회의에 적합하고 저희가 개발하려는 1대 다 연결을 수행하기에는 서버의 부하가 컸습니다. SRT, RTSP, MPEG-DASH는 호환성이 적었습니다.

대용량 트래픽을 염두에 둔 만큼 WebRTC는 부적합하다고 느꼈습니다. 기본적으로 지연 시간이 비교적 낮은 RTMP를 사용하고 미디어 서버에서 변환해 클라이언트에 HLS로 전달하는 방식을 채택하였습니다.

</div>
</details>

<details>
<summary>🧩 토스페이(토스페이먼츠x)</summary>
<div markdown="1">

토스 페이먼츠와 연동하면 훨씬 더 편하게 구현할 수 있었을 것입니다.

하지만 카카오페이 결제 연동과 일관성을 유지하고, 신용카드, 무통장입금, 페이코와 같은 불필요한 결제 수단을 배제하여 결제 프로세스를 단순화하고, 보안을 강화하기 위해 토스페이먼츠 대신 토스페이를 선택하였습니다. 이를 통해 관리 복잡성을 줄이고, 특정 결제 방식에 대한 제어권을 강화함으로써 시스템의 안정성과 사용자 경험을 향상시킬 수 있었습니다.

또한, 백엔드 개발자로서 단순히 결제 예제 코드를 프론트엔드에 연결하는 것을 넘어서 외부 API와의 통신에 대한 이해를 깊게 하고, 보다 안정적이고 효율적인 통신 구조를 설계하기 위한 학습의 일환이기도 하였습니다.

</div>
</details>


<div id ="trouble">
  
# ⚠️ 트러블 슈팅
<details>
<summary>💣 로드밸런서에서의 세션 데이터 관리</summary>
<div markdown="1">
대용량 트래픽을 대비해 두 개의 서버 인스턴스를 띄우고 로드밸런서를 사용해서 서버의 부하를 분산 시키려고 시도 하였습니다.

로드밸런서를 사용한 이후로, 로그인 과정에서 문제가 발생하게 되었습니다. Spring security filter에서 인증되지 않은 사용자라는 에러가 계속 잡히는 것을 확인 하였습니다.

로컬에서 테스트해봤을 때와 서버를 하나만 배포했을 때도 문제 없이 잘 진행 되었던 로그인이었어서, 서버 인스턴스를 하나 끄고 남은 하나에 로드밸런서를 연결해 테스트해보니 문제 없이 잘 해결되는 것을 보고 문제가 무엇인지 파악할 수 있었습니다.

저희는 소셜 로그인을 사용하고 있기 때문에 저희 서버에서 카카오나 구글로 먼저 요청을 보낸 후, 해당 소셜 서버에서 토큰 및 사용자 정보를 다시 저희 서버로 보내주게 됩니다.

1번 서버에서 소셜 로그인을 시도한 경우, 소셜 서버에서 다시 저희 서버로 정보를 보내줄 때 2번 서버로  redirect 되기 때문에 이런 문제가 발생할 수 있겠다는 생각이 들었습니다.

저희는 이러한 문제를 해결하기 위해 세션을 고정하는 Sticky Session을 사용하였습니다. Sticky Session을 사용하여서 로드밸런서가 세션 기간 동안 동일한 클라이언트의 request를 항상 동일한 서버로 라우팅될 수 있도록 해주는 방식으로 해당 문제를 해결하였습니다.

</div>
</details>

<details>
<summary>💣 Transaction 전파 전략 Required_New를 사용할 때 주의 할 점</summary>
<div markdown="1">
프로젝트에서 재고 관리를 할 때 동시성 제어를 적용하기 위해 분산 락을 사용했고, 분산 락을 걸어주는 로직에 트랜잭션을 Required_New를 사용하였습니다.

주문 재고 트랜잭션 **상위에 트랜잭션이 있는 존재하는 경우, 트랜잭션이 분산락을 반납하기 전이 아닌 상위 트랜잭션 영역까지 전파되므로 상위 트랜잭션 커밋 이전에 락을 반납하면 다른 트랜잭션이 커밋 이전의 데이터를 읽어 여전히 데이터 부정합 문제가 발생**할 수 있다고 생각했습니다. 따라서 새로운 트랜잭션을 만들어 주문 시 재고를 차감하고, 차감 후 커밋이 된 다음 락을 반납할 수 있도록 옵션으로 REQUIRES_NEW를 적용했습니다.

그러나 해당 코드를 테스트를 해보았을 때 문제가 발생했습니다. 한번에 100개의 요청이 들어온다고 가정하고 스레드 100개로 동시에 주문 요청을 보냈지만 100개의 스레드가 분산 락 획득을 시도하지 못 했고 10개만 시도한 것을 확인했습니다.

해당 문제가 발생한 이유는 JDBC 커넥션 풀의 개수가 10개로 제한 되어있기 때문에 발생한 문제라는 것을 파악했습니다. SpringBoot 2.x 이후 버전부터 HikariCP를 기본 JDBC 커넥션 풀로 사용하고 있는데 이때 maximum pool size의 default 값은 10입니다.

10개의 락 획득 시도를 했을 때 주문 트랜잭션에서 이미 10개의 커넥션을 획득하여 주문을 생성하고 그 다음 락 획득 시도로 넘어갈 수 있었고 나머지 90개의 요청 스레드는 계속 대기하고 있는 상태였습니다. 이 때, 하나의 스레드에서 분산 락을 획득한다고 하더라도 REQUIES_NEW 옵션으로 새로운 트랜잭션으로 커넥션을 다시 획득하려고 할 것이고 10개의 커넥션이 모두 사용되어 있기 때문에 분산 락을 획득한 트랜잭션 마저 커넥션을 대기하고 있어 앞으로 진행되지 못하게 되는 것이 문제였습니다.

해당 문제를 해결하기 위해 분산 락 대신 비관적 락을 사용하여 주문 재고 차감 시 정합성을 지키거나, maximum pool size를 늘리거나 상위 트랜잭션이 존재할 때, 분산 락에 Required_New를 적용하지 않는 방식 중 분산 락에는 Required_New를 적용하지 않기로 결정하였습니다. 개발 시간에 제한이 있었기 때문에 상위 트랜잭션이 존재하지 않는 상황에서 분산 락이 사용되는 방식으로 일단 해결을 해두었고, 추후에는 비관적 락을 사용해 해당 문제를 해결해보려고 합니다.
</div>
</details>

<details>
<summary>💣 토스 페이 WAN 연결 문제</summary>
<div markdown="1">
토스페이로 결제를 연결 할 때 url을 잘 넣어줬음에도 불구하고 ‘retUrl’ 또는 ‘retCancelUrl’이 없다는 오류가 있었습니다.

많은 삽질 끝에 WAN 환경의 url로만 연결이 가능하다는 것을 알게되었습니다. 튜터님의 도움을 받아 집의 공유기 IP를 포트포워딩하고 DDNS 설정을 완료하고 해당 IP를 등록하여 해결할 수 있었습니다.

![image](https://github.com/user-attachments/assets/c7eb5c93-1bc6-45d4-a949-bcf572c71d65)
![image](https://github.com/user-attachments/assets/c62f17ce-c3c9-4684-bbd6-77d2e2f2249d)
</div>
</details>


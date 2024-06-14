# 선착순 쿠폰 발급 서비스

### 쿠폰 발급 서비스는 한정된 수량의 쿠폰을 먼저 신청한 사용자에게 제공하는 서비스입니다.

# 📣 요구 사항
<div>
  1️⃣ 주어진 기간 내에 발급 <br>
  2️⃣ 선착순 쿠폰은 유저당 1번만 발급 <br>
  3️⃣ 선착순 쿠폰의 최대 발급 수량 설정 <br>
</div>

# ⚙️ 기술 스택

### Back-End

<div>
  <img alt="Java" src ="https://img.shields.io/badge/Java-007396.svg?&style=for-the-badge&logo=Java&logoColor=white"/>
  <img alt="Spring Boot" src ="https://img.shields.io/badge/Spring Boot-6DB33F.svg?&style=for-the-badge&logo=Spring Boot&logoColor=white"/>
  <img alt="Gradle" src ="https://img.shields.io/badge/Gradle-02303A.svg?&style=for-the-badge&logo=Gradle&logoColor=white"/>
  <img alt="Spring Security" src ="https://img.shields.io/badge/Spring Security-6DB33F.svg?&style=for-the-badge&logo=Spring Security&logoColor=white"/>
</div>
<div>
  <img alt="Amazon RDS" src="https://img.shields.io/badge/Amazon RDS-527FFF?style=for-the-badge&logo=Amazon RDS&logoColor=white"/>
  <img alt="MySQL" src ="https://img.shields.io/badge/MySQL-4479A1.svg?&style=for-the-badge&logo=MySQL&logoColor=white"/>
  <img alt="Hibernate" src ="https://img.shields.io/badge/Hibernate-59666C.svg?&style=for-the-badge&logo=Hibernate&logoColor=white"/>
</div>
<div>
  <img alt="Amazon AWS" src="https://img.shields.io/badge/Amazon AWS-232F3E?style=for-the-badge&logo=Amazon AWS&logoColor=white"/>
  <img alt="Amazon EC2" src="https://img.shields.io/badge/Amazon EC2-FF4F8B?style=for-the-badge&logo=Amazon EC2&logoColor=white"/>
</div>

<br />
<br />  

## ☁️ 프로젝트 주요 기능 

### ➀ 쿠폰 발급 기능
  - 쿠폰 발급 기간 검증
    - 쿠폰 발급 수량 검증
      - 쿠폰 전체 발급 수량
      - 중복 발급 요청 검증
<br>

  - 쿠폰 발급
      - 쿠폰 발급 수량 증가
      - 쿠폰 발급 기록 저장
        - 쿠폰 ID
        - 유저 ID
<br />
<br />

# ❗ 프로젝트 구현의 목표
- 정확한 발급 수량 제어(동시성 이슈 처리)
- 높은 처리량


<br />
<br />

# 📄 프로젝트 구조
- 기본 구조
![api 구조](https://github.com/Bae-Ji-Won/FirstCome_Coupon/assets/82360230/cefaffff-0d89-4ce5-b3e6-ad954c1118c8)

- 동시성 처리 구조
![트래픽 처리](https://github.com/Bae-Ji-Won/FirstCome_Coupon/assets/82360230/4dfe636f-d24b-43b1-b52c-732d12fa4b8c)

- Redis 활용하여 동시성 처리 구조
![image](https://github.com/Bae-Ji-Won/FirstCome_Coupon/assets/82360230/061c63e3-2a1c-4bec-a981-36aad9eda15d)

(1) N명의 유저가 요청을 보냄
(2) API 서버에서는 N개의 요청을 처리
(3) Redis에서 요청을 처리하고 쿠폰 발급 대상을 저장
(4) 쿠폰 발급 처리 기능에서 Redis의 쿠폰 발급 대상을 조회하여 발급 처리

<br/>
<br/>
<br/>
<br/>

PS. ![네고왕 이벤트 선착순 쿠폰 시스템](https://fastcampus.co.kr/dev_online_traffic_data)


# 상품 마켓 백엔드 과제

## 프로젝트 개요
상품 마켓의 백엔드 시스템을 구축하는 프로젝트입니다.

## 기술 스택
- Java 17
- Spring Boot 3.2.3
- MariaDB 10.x
- JPA
- Maven

## 주요 기능
1. 상품 조회
   - 구매 가능한 상품 목록 조회
2. 장바구니
   - 상품 추가/수정/삭제
   - 수량 관리
3. 주문 및 결제
   - 주문 제출
   - 결제 처리
4. 주문 내역 조회
   - 사용자의 완료된 주문 기록 조회

## 실행 방법
1. MariaDB 설치 및 실행
2. 데이터베이스 생성
   ```sql
   CREATE DATABASE product_db;
   ```
3. application.yml 파일에서 데이터베이스 접속 정보 설정
4. 프로젝트 빌드 및 실행
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

## API 문서
API 문서는 프로젝트 실행 후 다음 URL에서 확인할 수 있습니다:
- http://localhost:8080/swagger-ui.html 
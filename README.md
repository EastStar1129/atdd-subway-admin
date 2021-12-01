<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-%3E%3D%205.5.0-blue">
  <img alt="node" src="https://img.shields.io/badge/node-%3E%3D%209.3.0-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-admin">
</p>

<br>

# 지하철 노선도 미션

[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

<br>

## 🚀 Getting Started

### Install

#### npm 설치

```
cd frontend
npm install
```

> `frontend` 디렉토리에서 수행해야 합니다.

### Usage

#### webpack server 구동

```
npm run dev
```

#### application 구동

```
./gradlew bootRun
```

<br>

## ✏️ Code Review Process

[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/next-step/atdd-subway-admin/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/next-step/atdd-subway-admin/blob/master/LICENSE.md) licensed.

# Step1

## 요구사항

- 지하철 노선 관련 기능의 인수 테스트 작성하기
    - `LineAcceptanceTest`를 모두 완성시킨다
- 지하철 노선 관련 기능 구현하기
    - 인수 테스트가 모두 성공할 수 있도록 `LineController`를 통해 요청을 받고 처리하는 기능을 구현한다
- 인수 테스트 리팩터링
    - 인수 테스트의 각 스텝들을 메서드로 분리하여 재사용한다
        - ex) 인수 테스트 요청 로직 중복 제거 등

## 개발 요구사항

- 지하철 노선
    - 각 기능별 인수테스트 작성
    - 기능 구현
        - 생성
        - 목록 조회
        - 조회
        - 수정
        - 삭제

# Step2

## 요구사항

### API 변경 대응하기

- 노선 생성 시 종점역(상행, 하행) 정보를 요청 파라미터에 함께 추가하기
    - 두 종점역은 **구간**의 형태로 관리되어야 함
- 노선 조회 시 응답 결과에 역 목록 추가하기
    - **상행역 부터 하행역 순으로 정렬되어야 함**

## 개발 요구사항

- 구간(Section)
    - 두 역간의 연결 정보
    - 두 역간의 거리 정보를 포함
- 지하철 노선
    - 노선을 생성시 상행 역과 하행 역의 정보를 입력받음
    - 노선은 여러 구간에 대한 정보를 가지고 있음

## Step3

### 요구사항

- 지하철 구간 등록 기능을 구현
    - 구간 등록 케이스
        - 역과 역 사이에 새로운 역을 등록 할 경우
            - 새로운 길이를 뺀 나머지를 새롭게 추가된 역과의 길이로 설정
        - 새로운 역을 상행 종점으로 등록할 경우
            - 상행 종점이 바뀌며 해당 구간이 등록된다
        - 새로운 역을 하행 종점으로 등록할 경우
            - 하행 종점이 바뀌며 해당 구간이 등록된다
    - 구간 등록시 예외 케이스 고려 사항
        - 역 사이에 새로운 역을 등록할 경우 기존 역 사이 길이보다 크거나 같으면 등록 할 수 없음
        - 상행역과 하행역이 이미 노선에 모두 등록되어 있다면 추가할 수 없음
    - 상행역과 하행역 둘 중 하나도 노선에 포함되어 있지 않으면 추가할 수 없음

## Step4

### 요구사항

- 노선의 구간을 제거하는 기능을 구현
    - 종점이 제거될 경우 다음으로 오던 역이 종점이 됨
    - 중간역이 제거될 경우 재배치를 함
        - 노선에 A - B - C 역이 연결되어 있을 때 B역을 제거할 경우 A - C로 재배치 됨
        - 거리는 두 구간의 거리의 합으로 정함
    - 노선의 마지막 구간은 제거 불가능 함
- 구간 삭제 시 예외 케이스를 고려하기
    - 기능 설명을 참고하여 예외가 발생할 수 있는 경우를 검증할 수 있는 인증 테스트 생성

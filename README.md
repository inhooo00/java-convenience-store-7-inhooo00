# java-convenience-store-precourse

# 🚀 기능 구현 목록

## BonusProduct (보너스 상품)
- 보너스 상품 추가 여부 확인한다.
- 보너스 상품을 받을 수 있는지 확인한다.

## Customer(구매자)
- 상품 더 살건지 판단한다.

## Employee(편의점 직원)

- 재고 확인 및 차감
    - 주문 내역을 기반으로 결제한다.
    - 맴버쉽 할인 적용 여부 확인한다.

## InventoryManager (재고 관리)
- 상품 정보 로드
    - src/main/resources/products.md 파일에서 상품 정보를 읽어와 저장한다.
    - 상품 정보는 상품명, 가격, 초기 재고 수량, 프로모션 정보를 포함한다.
    - 파일 형식이 올바르지 않거나 데이터가 누락된 경우 예외를 발생시킨다.
- 프로모션 상품을 먼저 조회하고, 재고가 없으면 일반 상품으로 조회
- 재고 조회
    - 특정 상품의 현재 재고 수량을 반환한다.
    - 편의점에 보유 중인 모든 상품의 이름, 가격, 재고 상태, 프로모션 여부를 출력한다.

## MembershipDiscountManager (멤버십 할인 관리)

- 멤버십 할인 계산
    - 결제 금액에 대해 멤버십 할인을 계산하여 반환한다.
    - 멤버십 할인의 최대 한도는 8,000원으로 설정한다.
    - 할인 금액을 1000원단위로 내림 처리한다.
- 할인 적용 가능 여부 확인
    - 멤버십 할인이 적용 가능한 경우에만 할인을 계산하여 반환한다.

## OrderProcessor (주문)

- 주문 생성
    - 개별 주문 항목을 처리한다.
    - 상품 찾기와 검증을 포함하여 상품의 가격을 설정한다.
- 최종적으로 프로모션 할인을 적용한다.
- 프로모션 재고 처리: 부족하면 일반 재고에서 차감한다.

## Product (상품)

- 상품 생성
  - 상품명, 가격, 재고 수량, 프로모션 여부를 초기화한다
  - 물류 이상 요구시 오류를 발생시킨다.
  - 상품을 빼간다.

## Promotion (프로모션)

- 프로모션 생성
    - 프로모션 종류에 따라 프로모션을 생성한다.
    - 이벤트 기간인지 확인한다.

## PromotionManager (프로모션 관리)

- 프로모션을 정보 로드
  - src/main/resources/promotions.md 파일에서 프로모션 정보를 읽어와 저장한다.
  - 프로모션 정보는 프로모션명, 할인율, 할인 조건, 할인 대상 상품을 포함한다.


## 입력

- 상품 선택 및 수량 입력
    - 상품명과 수량을 입력받아 장바구니에 추가한다.
    - 존재하지 않는 상품을 입력한 경우 예외를 발생시킨다.
    - 입력 형식이 올바르지 않은 경우 예외를 발생시킨다.
- 멤버십 할인 여부 확인
    - 멤버십 할인을 받을지 여부를 입력받는다.
    - 잘못된 값을 입력할 경우 예외를 발생시킨다.
- 추가 구매 여부 확인
    - 구매가 완료된 후 추가 구매 여부를 입력받는다.
    - 잘못된 값을 입력할 경우 예외를 발생시킨다.

## 출력

- 상품 목록 출력
    - 편의점에 있는 모든 상품의 이름, 가격, 재고 상태, 프로모션 여부를 출력한다.
- 영수증 출력
    - 구매 내역을 포함한 영수증을 고객에게 출력한다.
- 총 결제 금액 출력
    - 최종 결제 금액과 할인 내역을 출력한다.

## 테스트 코드
- BonusProduct 테스트
- Customer 테스트
- Employee 테스트
- InventoryManager 테스트
- MembershipDiscountManager 테스트
- OrderProcessor 테스트
- Product 테스트
- Promotion 테스트
- PromotionManager 테스트

## 예외 처리
- [콜라-3],[에너지바-5] 형식으로 입력하지 않으면 예외를 발생시킨다.
- 구매할 상품과 수량 형식이 올바르지 않은 경우 예외를 발생시킨다.
- 존재하지 않는 상품을 입력한 경우 예외를 발생시킨다.
- 구매 수량이 재고 수량을 초과한 경우 예외를 발생시킨다.
- 구매 수량이 0 이하인 경우 예외를 발생시킨다.
- 멤버십 할인 여부를 Y/N으로 입력하지 않으면 예외를 발생시킨다.
- 추가 구매 여부를 Y/N으로 입력하지 않으면 예외를 발생시킨다.
- 추가 구매할 상품과 수량 형식이 올바르지 않은 경우 예외를 발생시킨다.
- 안내 문구 Y/N으로 입력하지 않으면 예외를 발생시킨다.

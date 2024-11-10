package store.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import store.dto.OrderComponents;
import store.dto.OrderDto;
import store.dto.OrderTotals;
import store.view.InputView;

public class Employee {

    private final InputView inputView;
    private final MembershipDiscountManager membershipDiscountManager;
    private final OrderProcessor orderProcessor;

    public Employee(InputView inputView, MembershipDiscountManager membershipDiscountManager,
                    OrderProcessor orderProcessor) {
        this.inputView = inputView;
        this.membershipDiscountManager = membershipDiscountManager;
        this.orderProcessor = orderProcessor;
    }

    public OrderDto processOrder() {
        while (true) {
            try {
                String productSelection = inputView.requestValidProductSelection();
                String[] items = productSelection.split(",");
                OrderComponents orderComponents = initializeOrderComponents();
                OrderTotals orderTotals = processOrderItems(items, orderComponents.orderedProducts(), orderComponents.bonusProducts(), orderComponents.productPrices());

                // 일반 상품의 총 금액을 계산하여 멤버십 할인 적용
                double nonPromotionTotal = orderTotals.nonPromotionTotal();
                double totalMembershipDiscount = requestAndApplyMembershipDiscount(nonPromotionTotal);

                return new OrderDto(
                        orderComponents.orderedProducts(),
                        orderComponents.productPrices(),
                        orderTotals.totalOrderPrice(),
                        orderTotals.totalPromotionDiscount(),
                        totalMembershipDiscount,
                        orderComponents.bonusProducts()
                );

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage()); // 예외 메시지 출력 후 다시 입력 요청
            }
        }
    }

    // 주문 구성 요소들을 초기화하는 메서드
    private OrderComponents initializeOrderComponents() {
        Map<String, Integer> productPrices = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> orderedProducts = new LinkedHashMap<>();
        Map<String, Integer> bonusProducts = new HashMap<>();

        return new OrderComponents(orderedProducts, productPrices, bonusProducts);
    }

    private OrderTotals processOrderItems(String[] items, LinkedHashMap<String, Integer> orderedProducts, Map<String, Integer> bonusProducts, Map<String, Integer> productPrices) {
        double totalOrderPrice = 0;
        double totalPromotionDiscount = 0;
        double nonPromotionTotal = 0;

        for (String item : items) {
            OrderTotals totals = orderProcessor.processOrderItem(item, orderedProducts, bonusProducts, productPrices);
            totalOrderPrice += totals.totalOrderPrice();
            totalPromotionDiscount += totals.totalPromotionDiscount();

            // 프로모션이 없는 상품의 금액을 nonPromotionTotal에 추가
            if (totals.totalPromotionDiscount() == 0) {
                nonPromotionTotal += totals.totalOrderPrice();
            }
        }
        return new OrderTotals(totalOrderPrice, totalPromotionDiscount, nonPromotionTotal);
    }

    private double requestAndApplyMembershipDiscount(double nonPromotionTotal) {
        boolean applyDiscount = inputView.requestMembershipDiscount();
        return membershipDiscountManager.applyMembershipDiscount(nonPromotionTotal, applyDiscount);
    }
}

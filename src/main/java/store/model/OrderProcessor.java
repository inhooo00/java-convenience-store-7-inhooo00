package store.model;

import java.util.LinkedHashMap;
import java.util.Map;
import store.dto.OrderTotals;
import store.dto.QuantityUpdateDto;
import store.exception.ExceptionMessage;
import store.view.InputView;

public class OrderProcessor {
    private final InventoryManager inventoryManager;
    private final InputView inputView;
    private final BonusProduct bonusProduct;

    public OrderProcessor(InventoryManager inventoryManager, InputView inputView, BonusProduct bonusProduct) {
        this.inventoryManager = inventoryManager;
        this.inputView = inputView;
        this.bonusProduct = bonusProduct;
    }

    // 개별 주문 항목 처리 메서드
    public OrderTotals processOrderItem(String item, LinkedHashMap<String, Integer> orderedProducts,
                                        Map<String, Integer> bonusProducts, Map<String, Integer> productPrices) {
        String[] productData = item.replace("[", "").replace("]", "").split("-",-1);
        String productName = productData[0];
        int requestedQuantity = Integer.parseInt(productData[1]);

        Product product = findAndValidateProduct(productName, productPrices);
        if (product == null) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다."); // 예외 발생
        }

        QuantityUpdateDto quantities = bonusProduct.handleBonusProductAddition(product, requestedQuantity, orderedProducts);
        return applyPromotionDiscounts(product, orderedProducts, quantities, bonusProducts);
    }

    // 상품 찾기와 검증을 포함하여 상품의 가격을 설정하는 메서드
    private Product findAndValidateProduct(String productName, Map<String, Integer> productPrices) {
        Product product = findAndValidatePromotionOrNormalProduct(productName);
        if (product != null) {
            productPrices.put(product.getName(), product.getPrice());
        }
        return product;
    }

    // 최종적으로 프로모션 할인을 적용하는 메서드
    private OrderTotals applyPromotionDiscounts(Product product, LinkedHashMap<String, Integer> orderedProducts,
                                                QuantityUpdateDto quantities, Map<String, Integer> bonusProducts) {
        int inputQuantity = quantities.inputQuantity();
        int requestedQuantity = quantities.requestedQuantity();

        return handlePromotionProduct(product, orderedProducts, requestedQuantity, inputQuantity, bonusProducts);
    }

    // 프로모션 및 일반 상품 검색 후 유효성 검증 메서드
    private Product findAndValidatePromotionOrNormalProduct(String productName) {
        Product product = inventoryManager.findPromotionProduct(productName, true);
        if (product == null) {
            product = inventoryManager.findProduct(productName);
        }
        if (product == null) {
            System.out.println(ExceptionMessage.NON_EXISTENT_PRODUCT.format());
        }
        return product;
    }

    // 프로모션 재고 처리 메서드: 부족하면 일반 재고에서 차감 로직 - 역할과 책임 분리 필요
    private OrderTotals handlePromotionProduct(Product product, LinkedHashMap<String, Integer> orderedProducts,
                                               int requestedQuantity, int inputQuantity,
                                               Map<String, Integer> bonusProducts) {
        double orderPrice = 0;
        double promotionDiscount = 0;
        double nonPromotionTotal = 0;
        int availableStock = product.getStock();

        if (product.getPromotion() != null && product.getPromotion().isActive()) {
            int buyQuantity = product.getPromotion().getBuyQuantity();

            if (requestedQuantity > availableStock) {
                int remainingQuantity = requestedQuantity - availableStock;
                int quantityNotApplicableToPromotion = inputQuantity - ((product.getStock() / (buyQuantity + 1)) * (buyQuantity + 1));
                requestedQuantity = confirmAndAdjustStockForNormalProduct(product, remainingQuantity, quantityNotApplicableToPromotion, requestedQuantity);
            }

            promotionDiscount = calculatePromotionDiscount(product, requestedQuantity);
            bonusProducts.put(product.getName(), requestedQuantity / (product.getPromotion().getBuyQuantity() + 1));
        } else {
            nonPromotionTotal += product.getPrice() * inputQuantity;
            bonusProducts.put(product.getName(), 0);
        }

        orderPrice += product.getPrice() * inputQuantity;
        orderedProducts.put(product.getName(), inputQuantity);

        try {
            product.decreaseStock(requestedQuantity, inventoryManager.findPromotionProduct(product.getName(), false));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            inputView.requestValidProductSelection(); // 재입력 요청
            return new OrderTotals(0, 0, 0); // 에러 발생으로 인한 주문 취소
        }
        return new OrderTotals(orderPrice, promotionDiscount, nonPromotionTotal);
    }

    // 일반 상품 재고를 확인하고 부족분을 처리하는 메서드
    private int confirmAndAdjustStockForNormalProduct(Product product, int remainingQuantity, int quantityNotApplicableToPromotion, int requestedQuantity) {
        Product normalProduct = inventoryManager.findPromotionProduct(product.getName(), false);
        if (normalProduct != null && normalProduct.getStock() >= remainingQuantity) {
            if (inputView.confirmPurchaseWithoutPromotion(product.getName(), quantityNotApplicableToPromotion)) {
                normalProduct.decreaseStock(remainingQuantity);
                requestedQuantity = product.getStock(); // 프로모션 재고의 최대치로 설정
            }
        }
        return requestedQuantity;
    }

    // 프로모션 할인 계산 메서드
    private double calculatePromotionDiscount(Product product, int requestedQuantity) {
        return product.getPrice() * (requestedQuantity / (product.getPromotion().getBuyQuantity() + 1));
    }
}

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
        String[] productData = item.replace("[", "").replace("]", "").split("-");
        String productName = productData[0];
        int requestedQuantity = Integer.parseInt(productData[1]);

        Product product = findAndValidateProduct(productName, productPrices);
        if (product == null) {
            return new OrderTotals(0, 0);
        }

        QuantityUpdateDto quantities = bonusProduct.handleBonusProductAddition(product, requestedQuantity,
                orderedProducts);
        return applyPromotionDiscounts(product, orderedProducts, quantities, bonusProducts);
    }

    // 상품 찾기와 검증을 포함하여 상품의 가격을 설정하는 메서드
    private Product findAndValidateProduct(String productName, Map<String, Integer> productPrices) {
        Product product = findAndValidatePromotionOrNormalProduct(productName);
        if (product != null) {
            productPrices.put(product.getName(), product.getPrice());
        } else {
            System.out.println(ExceptionMessage.NON_EXISTENT_PRODUCT.format());
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

    // 프로모션 재고 처리 메서드: 부족하면 일반 재고에서 차감
    private OrderTotals handlePromotionProduct(Product product, LinkedHashMap<String, Integer> orderedProducts,
                                               int requestedQuantity, int inputQuantity,
                                               Map<String, Integer> bonusProducts) {
        double orderPrice = 0;
        double promotionDiscount = 0;
        int availableStock = product.getStock();

        if (product.getPromotion() != null && product.getPromotion().isActive()) {
            int buyQuantity = product.getPromotion().getBuyQuantity();

            if (requestedQuantity > availableStock) {
                int remainingQuantity = requestedQuantity - availableStock;
                int quantityNotApplicableToPromotion =
                        inputQuantity - ((product.getStock() / (buyQuantity + 1)) * (buyQuantity + 1));

                Product normalProduct = inventoryManager.findPromotionProduct(product.getName(), false);
                if (normalProduct != null && normalProduct.getStock() >= remainingQuantity) {
                    if (inputView.confirmPurchaseWithoutPromotion(product.getName(),
                            quantityNotApplicableToPromotion)) {
                        normalProduct.decreaseStock(remainingQuantity);
                        requestedQuantity = availableStock;
                    }
                }
            }

            promotionDiscount +=
                    product.getPrice() * (requestedQuantity / (product.getPromotion().getBuyQuantity() + 1));
            bonusProducts.put(product.getName(),
                    requestedQuantity / (product.getPromotion().getBuyQuantity() + 1));
        } else {
            bonusProducts.put(product.getName(), 0);
        }

        orderPrice += product.getPrice() * inputQuantity;
        orderedProducts.put(product.getName(), inputQuantity);
        try {
            product.decreaseStock(requestedQuantity, inventoryManager.findPromotionProduct(product.getName(), false));
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            inputView.requestValidProductSelection(); // 재입력 요청
            return new OrderTotals(0, 0); // 에러 발생으로 인한 주문 취소
        }
        return new OrderTotals(orderPrice, promotionDiscount);
    }
}

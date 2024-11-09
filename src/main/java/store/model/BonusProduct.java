package store.model;

import store.dto.QuantityUpdateDto;
import store.view.InputView;
import java.util.LinkedHashMap;

public class BonusProduct {

    private final InputView inputView;

    public BonusProduct(InputView inputView) {
        this.inputView = inputView;
    }

    // 보너스 상품 추가 여부 처리 메서드
    public QuantityUpdateDto handleBonusProductAddition(Product product, int requestedQuantity,
                                                        LinkedHashMap<String, Integer> orderedProducts) {
        int inputQuantity = requestedQuantity;
        if (isEligibleForBonus(product, inputQuantity) && inputView.confirmBonusProductAddition(product.getName())) {
            inputQuantity += 1;
            requestedQuantity += 1;
            orderedProducts.put(product.getName(), inputQuantity);
        }
        return new QuantityUpdateDto(inputQuantity, requestedQuantity);
    }

    // 보너스 상품을 받을 수 있는지 확인하는 메서드
    private boolean isEligibleForBonus(Product product, int inputQuantity) {
        return product.getPromotion() != null
                && inputQuantity == product.getPromotion().getBuyQuantity()
                && inputQuantity < product.getStock();
    }
}

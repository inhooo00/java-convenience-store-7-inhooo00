package store.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public record OrderDto(
        LinkedHashMap<String, Integer> orderedProducts,
        Map<String, Integer> productPrices,
        double totalOrderPrice,
        double totalPromotionDiscount,
        double totalMembershipDiscount,
        Map<String, Integer> bonusProducts
) {
}

package store.dto;

import java.util.LinkedHashMap;
import java.util.Map;

public record OrderComponents(
        LinkedHashMap<String, Integer> orderedProducts,
        Map<String, Integer> productPrices,
        Map<String, Integer> bonusProducts
) {}

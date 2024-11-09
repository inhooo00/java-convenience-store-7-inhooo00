package store.dto;

public record OrderTotals(
        double totalOrderPrice,
        double totalPromotionDiscount
) {
}

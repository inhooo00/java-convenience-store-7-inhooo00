package store.model;

public class MembershipDiscountManager {

    private static final double DISCOUNT_RATE = 0.3;
    private static final int MAX_DISCOUNT = 8000;

    public double calculateDiscount(double amount) {
        double discount = Math.floor(amount * DISCOUNT_RATE / 1000) * 1000;
        return Math.min(discount, MAX_DISCOUNT);
    }

    public boolean canApplyDiscount(double amount) {
        return amount > 0;
    }

    public double applyMembershipDiscount(double totalOrderPrice, double totalPromotionDiscount, boolean applyDiscount) {
        double applicableAmount = totalOrderPrice - totalPromotionDiscount;
        return applyDiscount && canApplyDiscount(applicableAmount) ? calculateDiscount(applicableAmount) : 0;
    }
}

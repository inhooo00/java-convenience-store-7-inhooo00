package store.model;

public class MembershipDiscountManager {

    private static final double DISCOUNT_RATE = 0.3;
    private static final int MAX_DISCOUNT = 8000;

    public double calculateDiscount(double amount) {
        double discount = amount * DISCOUNT_RATE;
        return Math.min(discount, MAX_DISCOUNT);
    }

    public boolean canApplyDiscount(double amount) {
        return amount > 0;
    }

    public double applyMembershipDiscount(double nonPromotionTotal, boolean applyDiscount) {
        return applyDiscount && canApplyDiscount(nonPromotionTotal) ? calculateDiscount(nonPromotionTotal) : 0;
    }
}

package store.view;

import java.util.List;
import java.util.Map;
import store.model.Product;
import java.text.DecimalFormat;

public class OutputView {

    public void printWelcomeMessage() {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
    }

    public void printInventory(List<Product> inventory) {
        inventory.forEach(this::printProductInfo);
    }

    private void printProductInfo(Product product) {
        String priceInfo = formatPrice(product.getPrice());
        String promotionInfo = product.hasPromotion() ? product.getPromotion().getName() : "";

        if (product.getStock() == 0) {
            System.out.printf("- %s %s 재고 없음\n", product.getName(), priceInfo);
        } else {
            System.out.printf("- %s %s %d개 %s\n", product.getName(), priceInfo, product.getStock(), promotionInfo);
        }
    }

    public void printTotal(Map<String, Integer> orderedProducts,
                           Map<String, Integer> productPrices,
                           double total, double promotionDiscount,
                           double membershipDiscount, Map<String, Integer> bonusProducts) {
        printTotalHeader();
        printOrderedProducts(orderedProducts, productPrices);
        printBonusSection(bonusProducts);
        printTotalSummary(total, promotionDiscount, membershipDiscount);
    }

    private void printTotalHeader() {
        System.out.println("===========W 편의점=============");
        System.out.println("상품명\t\t수량\t금액");
    }

    private void printOrderedProducts(Map<String, Integer> orderedProducts, Map<String, Integer> productPrices) {
        orderedProducts.forEach((productName, quantity) -> {
            int price = productPrices.get(productName);
            System.out.printf("%s\t\t%d\t%s\n", productName, quantity, formatPrice(price * quantity));
        });
    }

    private void printBonusSection(Map<String, Integer> bonusProducts) {
        System.out.println("===========증\t정=============");
        bonusProducts.forEach((productName, bonusQuantity) -> {
            if (bonusQuantity > 0) {
                System.out.printf("%s\t\t%d\n", productName, bonusQuantity);
            }
        });
        System.out.println("==============================");
    }

    private void printTotalSummary(double total, double promotionDiscount, double membershipDiscount) {
        System.out.printf("총구매액\t\t%s\n", formatPrice((int) total));
        System.out.printf("행사할인\t\t\t-%s\n", formatPrice((int) promotionDiscount));
        System.out.printf("멤버십할인\t\t\t-%s\n", formatPrice((int) membershipDiscount));
        System.out.printf("내실돈\t\t\t%s\n", formatPrice((int) (total - promotionDiscount - membershipDiscount)));
        System.out.println("=================================");
    }

    private String formatPrice(int price) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(price) + "원";
    }
}

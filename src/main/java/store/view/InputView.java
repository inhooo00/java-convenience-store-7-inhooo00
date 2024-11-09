package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.exception.ExceptionMessage;
import store.util.validation.InputValidator;

public class InputView {

    public String requestProductSelection() {
        System.out.print("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1]): ");
        return Console.readLine();
    }

    public boolean requestMembershipDiscount() {
        System.out.print("멤버십 할인을 받으시겠습니까? (Y/N)");
        System.out.println();
        String response = Console.readLine();
        return response.equalsIgnoreCase("Y");
    }

    public boolean requestAdditionalPurchase() {
        System.out.print("추가 구매를 진행하시겠습니까? (Y/N)");
        System.out.println();
        String response = Console.readLine();
        return response.equalsIgnoreCase("Y");
    }

    public boolean confirmBonusProductAddition(String productName) {
        System.out.printf("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N) ", productName);
        System.out.println();
        String response = Console.readLine();
        return response.equalsIgnoreCase("Y");
    }

    public boolean confirmPurchaseWithoutPromotion(String productName, int quantity) {
        System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N) ", productName, quantity);
        System.out.println();
        String response = Console.readLine();
        return response.equalsIgnoreCase("Y");
    }

    public String requestValidProductSelection() {
        String productSelection = requestProductSelection();
        while (!InputValidator.isValidProductSelection(productSelection)) {
            System.out.println(ExceptionMessage.INVALID_FORMAT.format());
            productSelection = requestProductSelection();
        }
        return productSelection;
    }
}

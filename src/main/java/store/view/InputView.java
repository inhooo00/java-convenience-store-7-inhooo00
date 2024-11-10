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
        return requestYesOrNo("멤버십 할인을 받으시겠습니까? (Y/N)");
    }

    public boolean requestAdditionalPurchase() {
        return requestYesOrNo("추가 구매를 진행하시겠습니까? (Y/N)");
    }

    public boolean confirmBonusProductAddition(String productName) {
        return requestYesOrNo(String.format("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)", productName));
    }

    public boolean confirmPurchaseWithoutPromotion(String productName, int quantity) {
        return requestYesOrNo(String.format("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)", productName, quantity));
    }

    public String requestValidProductSelection() {
        String productSelection = requestProductSelection();
        while (!InputValidator.isValidProductSelection(productSelection)) {
            System.out.println(ExceptionMessage.INVALID_FORMAT.format());
            productSelection = requestProductSelection();
        }
        return productSelection;
    }

    private boolean requestYesOrNo(String message) {
        while (true) {
            System.out.print(message);
            System.out.println();
            String response = Console.readLine().trim();
            if (response.equalsIgnoreCase("Y")) {
                return true;
            } else if (response.equalsIgnoreCase("N")) {
                return false;
            } else {
                System.out.println("잘못된 입력입니다. Y 또는 N으로 응답해 주세요.");
            }
        }
    }
}

package store.exception;

public enum ExceptionMessage {
    INVALID_FORMAT("올바른 형식으로 입력해 주세요. 예: [콜라-3],[에너지바-5]"),
    INVALID_PRODUCT_FORMAT("구매할 상품과 수량 형식이 올바르지 않습니다."),
    NON_EXISTENT_PRODUCT("존재하지 않는 상품입니다."),
    EXCEEDS_STOCK_QUANTITY("구매 수량이 재고 수량을 초과했습니다."),
    INVALID_PURCHASE_QUANTITY("구매 수량은 0 이하일 수 없습니다."),
    INVALID_MEMBERSHIP_INPUT("멤버십 할인 여부는 Y/N으로 입력해야 합니다."),
    INVALID_ADDITIONAL_PURCHASE_INPUT("추가 구매 여부는 Y/N으로 입력해야 합니다."),
    INVALID_ADDITIONAL_PRODUCT_FORMAT("추가 구매할 상품과 수량 형식이 올바르지 않습니다."),
    INVALID_CONFIRMATION_INPUT("안내 문구는 Y/N으로 입력해야 합니다."),
    FILE_READ_ERROR("파일을 읽는 중 오류가 발생했습니다: %s"),
    PROMOTION_PARSING_ERROR("프로모션 정보를 파싱하는 중 오류가 발생했습니다: %s"),
    INVALID_QUANTITY("수량은 0 이상이어야 합니다."),
    INSUFFICIENT_STOCK("재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요."),;

    public static final String BASE_MESSAGE = "[ERROR] %s";
    private final String message;

    ExceptionMessage(String message) {
        this.message = String.format(BASE_MESSAGE, message);
    }

    public String format(Object... args) {
        return String.format(message, args);
    }
}

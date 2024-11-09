package store.util.validation;

import java.util.regex.Pattern;

public class InputValidator {

    private static final Pattern PRODUCT_SELECTION_PATTERN = Pattern.compile("\\[[^\\[\\]-]+-\\d+]");

    public static boolean isValidProductSelection(String productSelection) {
        String[] items = productSelection.split(",");
        for (String item : items) {
            if (!PRODUCT_SELECTION_PATTERN.matcher(item).matches()) {
                return false;
            }
        }
        return true;
    }
}

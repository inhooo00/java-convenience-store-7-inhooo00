package store.model;

import store.view.InputView;
import store.view.OutputView;

public class Customer {
    private final InputView inputView = new InputView();

    public boolean requestAdditionalPurchase() {
        return inputView.requestAdditionalPurchase();
    }
}

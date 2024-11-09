package store.model;

import store.exception.ExceptionMessage;

public class Product {

    private final String name;
    private final int price;
    private int stock;
    private final Promotion promotion;

    public Product(String name, int price, int stock, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.promotion = promotion;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public boolean hasPromotion() {
        return promotion != null;
    }

    public void decreaseStock(int quantity) {
        validateQuantity(quantity);
        this.stock -= quantity;
    }

    public void decreaseStock(int quantity, Product normalProduct) {
        validateQuantity(quantity);
        if (isStockSufficient(quantity)) {
            this.stock -= quantity;
        } else {
            handleStockShortage(quantity, normalProduct);
        }
    }

    private void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_QUANTITY.format());
        }
    }

    private boolean isStockSufficient(int quantity) {
        return this.stock >= quantity;
    }

    private void handleStockShortage(int quantity, Product normalProduct) {
        int shortage = quantity - this.stock;
        this.stock = 0;
        if (normalProduct != null && normalProduct.getStock() >= shortage) {
            normalProduct.decreaseStock(shortage);
        } else {
            throw new IllegalArgumentException(ExceptionMessage.INSUFFICIENT_STOCK.format());
        }
    }
}

package store.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import store.exception.ExceptionMessage;

public class InventoryManager {

    private final List<Product> inventory = new ArrayList<>();
    private final PromotionManager promotionManager;

    public InventoryManager(PromotionManager promotionManager) {
        this.promotionManager = promotionManager;
    }

    public void loadProductsFromFile(String filePath) {
        try (BufferedReader reader = createBufferedReader(filePath)) {
            reader.readLine();
            readAndAddProducts(reader);
        } catch (IOException e) {
            System.err.println(ExceptionMessage.FILE_READ_ERROR.format(e.getMessage()));
        }
    }

    private BufferedReader createBufferedReader(String filePath) throws IOException {
        return new BufferedReader(new FileReader(filePath));
    }

    private void readAndAddProducts(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            Product product = parseProduct(line);
            inventory.add(product);
        }
    }

    private Product parseProduct(String line) {
        String[] parts = line.split(",");
        String name = parts[0];
        int price = Integer.parseInt(parts[1]);
        int stock = Integer.parseInt(parts[2]);
        String promotionName = parsePromotionName(parts[3]);
        Promotion promotion = findPromotion(promotionName);

        return new Product(name, price, stock, promotion);
    }

    private String parsePromotionName(String promotionField) {
        if ("null".equals(promotionField)) {
            return null;
        }
        return promotionField;
    }

    private Promotion findPromotion(String promotionName) {
        if (promotionName == null) {
            return null;
        }
        return promotionManager.getPromotion(promotionName);
    }

    public List<Product> getInventory() {
        return inventory;
    }

    // 프로모션 상품을 먼저 조회하고, 재고가 없으면 일반 상품으로 조회
    public Product findProduct(String productName) {
        return findPromotionProduct(productName, true); // 기본적으로 프로모션 상품을 우선 조회
    }

    // 프로모션 여부를 체크할지
    public Product findPromotionProduct(String productName, boolean checkPromotion) {
        if (checkPromotion) {
            return findProductWithPromotion(productName);
        }
            return findProductWithoutPromotion(productName);
    }

    // 프로모션 상품 조회
    private Product findProductWithPromotion(String productName) {
        Product promoProduct = inventory.stream()
                .filter(product -> product.getName().equals(productName) && product.getPromotion() != null)
                .findFirst()
                .orElse(null);

        return getAvailableProduct(promoProduct, productName);
    }

    private Product getAvailableProduct(Product promoProduct, String productName) {
        if (isProductAvailable(promoProduct)) {
            return promoProduct;
        }
        return findProductWithoutPromotion(productName);
    }

    private boolean isProductAvailable(Product product) {
        return product != null && product.getStock() > 0;
    }

    // 일반 상품 조회
    private Product findProductWithoutPromotion(String productName) {
        return inventory.stream()
                .filter(product -> product.getName().equals(productName) && product.getPromotion() == null)
                .findFirst()
                .orElse(null);
    }
}

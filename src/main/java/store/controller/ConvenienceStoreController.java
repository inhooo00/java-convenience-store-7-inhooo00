package store.controller;

import store.dto.OrderDto;
import store.model.*;
import store.view.InputView;
import store.view.OutputView;

public class ConvenienceStoreController {
    private final PromotionManager promotionManager;
    private final InventoryManager inventoryManager;
    private final OutputView outputView;
    private final Customer customer;
    private final MembershipDiscountManager membershipDiscountManager;
    private final InputView inputView;
    private final BonusProduct bonusProduct;
    private final Employee employee;
    private final OrderProcessor orderProcessor;

    public ConvenienceStoreController() {
        this.promotionManager = new PromotionManager();
        this.inventoryManager = new InventoryManager(promotionManager);
        this.outputView = new OutputView();
        this.customer = new Customer();
        this.membershipDiscountManager = new MembershipDiscountManager();
        this.inputView = new InputView();
        this.bonusProduct = new BonusProduct(inputView);
        this.orderProcessor = new OrderProcessor(inventoryManager, inputView, bonusProduct);
        this.employee = new Employee(inputView, membershipDiscountManager, orderProcessor);
    }

    public void run() {
        loadInitialData();

        boolean continueShopping = true;
        while (continueShopping) {
            outputView.printWelcomeMessage();
            outputView.printInventory(inventoryManager.getInventory());

            // Employee 클래스에서 주문 처리 후 결과 반환받기
            OrderDto orderDTO = employee.processOrder();

            // 반환된 OrderDTO record를 통해 주문 결과 출력
            outputView.printTotal(
                    orderDTO.orderedProducts(),
                    orderDTO.productPrices(),
                    orderDTO.totalOrderPrice(),
                    orderDTO.totalPromotionDiscount(),
                    orderDTO.totalMembershipDiscount(),
                    orderDTO.bonusProducts()
            );

            continueShopping = customer.requestAdditionalPurchase();
            if (continueShopping) {
                outputView.printInventory(inventoryManager.getInventory());
            }
        }
    }

    private void loadInitialData() {
        String productsFilePath = "src/main/resources/products.md";
        String promotionsFilePath = "src/main/resources/promotions.md";

        promotionManager.loadPromotionsFromFile(promotionsFilePath);
        inventoryManager.loadProductsFromFile(productsFilePath);
    }
}

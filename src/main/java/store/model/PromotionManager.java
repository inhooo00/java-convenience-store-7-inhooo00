package store.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import store.exception.ExceptionMessage;

public class PromotionManager {

    private final Map<String, Promotion> promotions = new HashMap<>();

    public void loadPromotionsFromFile(String filePath) {
        try (BufferedReader reader = createBufferedReader(filePath)) {
            reader.readLine();
            readAndAddPromotions(reader);
        } catch (IOException e) {
            System.err.println(ExceptionMessage.FILE_READ_ERROR.format(e.getMessage()));
        }
    }

    private BufferedReader createBufferedReader(String filePath) throws IOException {
        return new BufferedReader(new FileReader(filePath));
    }

    private void readAndAddPromotions(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            Promotion promotion = parsePromotion(line);
            promotions.put(promotion.getName(), promotion);
        }
    }

    private Promotion parsePromotion(String line) {
        String[] parts = line.split(",");
        String name = parts[0];
        int buyQuantity = Integer.parseInt(parts[1]);
        int getQuantity = Integer.parseInt(parts[2]);
        LocalDate startDate = LocalDate.parse(parts[3]);
        LocalDate endDate = LocalDate.parse(parts[4]);

        return new Promotion(name, buyQuantity, getQuantity, startDate, endDate);
    }

    public Promotion getPromotion(String promotionName) {
        return promotions.get(promotionName);
    }
}
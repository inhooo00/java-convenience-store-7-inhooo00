package store.dto;

public record QuantityUpdateDto(
        int inputQuantity,
        int requestedQuantity
) {
}

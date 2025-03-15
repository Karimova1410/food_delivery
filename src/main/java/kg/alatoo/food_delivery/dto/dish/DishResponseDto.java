package kg.alatoo.food_delivery.dto.dish;

import kg.alatoo.food_delivery.enums.DishCategory;
import lombok.Builder;

@Builder
public record DishResponseDto(
    Long id,
    String name,
    String description,
    double price,
    DishCategory category,
    Long restaurantId) {
}

package kg.alatoo.food_delivery.dto.dish;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import kg.alatoo.food_delivery.enums.DishCategory;
import lombok.Builder;


@Builder
public record DishRequestDto(
    @Size(max = 100, message = "Name must be less than 100 characters")
    String name,

    @Size(max = 500, message = "Description must be less than 500 characters")
    String description,

    @Positive(message = "Price must be a positive value")
    double price,

    DishCategory category,

    @Positive(message = "Restaurant ID must be a positive value")
    Long restaurantId
) {
}

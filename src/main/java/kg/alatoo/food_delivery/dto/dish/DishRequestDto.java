package kg.alatoo.food_delivery.dto.dish;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import kg.alatoo.food_delivery.enums.DishCategory;
import lombok.Builder;


@Builder
public record DishRequestDto(
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    String name,

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must be less than 500 characters")
    String description,

    @Positive(message = "Price must be a positive value")
    double price,

    @NotNull(message = "Category is required")
    DishCategory category,

    @NotNull(message = "Restaurant ID is required")
    @Positive(message = "Restaurant ID must be a positive value")
    Long restaurantId
) {
}

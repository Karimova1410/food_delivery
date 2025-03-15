package kg.alatoo.food_delivery.dto.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import kg.alatoo.food_delivery.enums.OrderStatus;
import lombok.Builder;

@Builder
public record OrderRequestDto(
    @NotNull(message = "Client ID is required")
    @Positive(message = "Client ID must be a positive value")
    Long clientId,

    @NotNull(message = "Restaurant ID is required")
    @Positive(message = "Restaurant ID must be a positive value")
    Long restaurantId,

    @NotEmpty(message = "Dish IDs list cannot be empty")
    List<@Positive(message = "Each dish ID must be a positive value") Long> dishIds,

    @NotNull(message = "Order status is required")
    OrderStatus status,

    @Positive(message = "Courier ID must be a positive value")
    Long courierId
) {
}

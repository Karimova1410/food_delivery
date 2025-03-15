package kg.alatoo.food_delivery.dto.order;

import jakarta.validation.constraints.Positive;
import java.util.List;
import kg.alatoo.food_delivery.enums.OrderStatus;
import lombok.Builder;

@Builder
public record OrderRequestDto(
    @Positive(message = "Client ID must be a positive value")
    Long clientId,

    @Positive(message = "Restaurant ID must be a positive value")
    Long restaurantId,

    List<@Positive(message = "Each dish ID must be a positive value") Long> dishIds,

    OrderStatus status,

    @Positive(message = "Courier ID must be a positive value")
    Long courierId
) {
}

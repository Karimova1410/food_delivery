package kg.alatoo.food_delivery.dto.order;

import java.util.List;
import kg.alatoo.food_delivery.enums.OrderStatus;
import lombok.Builder;

@Builder
public record OrderResponseDto(
    Long id,
    Long clientId,
    Long restaurantId,
    List<Long> dishIds,
    OrderStatus status,
    Long courierId) {
}
package kg.alatoo.food_delivery.dto.restaurant;


import java.util.List;
import lombok.Builder;

@Builder
public record RestaurantResponseDto(
    Long id,
    String name,
    String address,
    List<Long> dishIds) {
}

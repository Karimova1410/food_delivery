package kg.alatoo.food_delivery.dto.restaurant;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;

@Builder
public record RestaurantRequestDto(
    @Size(max = 100, message = "Name must be less than 100 characters")
    String name,

    @Size(max = 200, message = "Address must be less than 200 characters")
    String address,

    List<@Positive(message = "Each dish ID must be a positive value") Long> dishIds
) {
}

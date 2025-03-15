package kg.alatoo.food_delivery.dto.user;

import kg.alatoo.food_delivery.enums.UserRole;
import lombok.Builder;

@Builder
public record UserResponseDto(
    Long id,
    String username,
    UserRole role) {
}

package kg.alatoo.food_delivery.dto.user;

import kg.alatoo.food_delivery.enums.Role;
import lombok.Builder;

@Builder
public record UserResponseDto(
    Long id,
    String username,
    Role role) {
}

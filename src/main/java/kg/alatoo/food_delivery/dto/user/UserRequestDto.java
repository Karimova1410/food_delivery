package kg.alatoo.food_delivery.dto.user;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kg.alatoo.food_delivery.enums.Role;
import lombok.Builder;

@Builder
public record UserRequestDto(
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username,

    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
        message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace"
    )
    String password,

    Role role
) {

}
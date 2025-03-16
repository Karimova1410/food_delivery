package kg.alatoo.food_delivery.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import kg.alatoo.food_delivery.dto.user.UserRequestDto;
import kg.alatoo.food_delivery.enums.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserRequestDtoTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void whenUsernameIsTooShort_thenValidationFails() {
    UserRequestDto user = UserRequestDto.builder()
        .username("ab")
        .password("Roza2004@")
        .role(UserRole.CLIENT)
        .build();

    Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(user);
    assertEquals(1, violations.size());
    assertEquals("Username must be between 3 and 50 characters", violations.iterator().next().getMessage());
  }

  @Test
  void whenUsernameIsTooLong_thenValidationFails() {
    UserRequestDto user = UserRequestDto.builder()
        .username("a".repeat(51))
        .password("Roza2004@")
        .role(UserRole.CLIENT)
        .build();

    Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(user);
    assertEquals(1, violations.size());
    assertEquals("Username must be between 3 and 50 characters", violations.iterator().next().getMessage());
  }

  @Test
  void whenPasswordIsTooShort_thenValidationFails() {
    UserRequestDto user = UserRequestDto.builder()
        .username("RozaKarimova")
        .password("Roza21@")
        .role(UserRole.CLIENT)
        .build();

    Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(user);
    assertEquals(2, violations.size());
    assertEquals("Password must be between 8 and 100 characters", violations.iterator().next().getMessage());
  }

  @Test
  void whenPasswordDoesNotMeetComplexityRequirements_thenValidationFails() {
    UserRequestDto user = UserRequestDto.builder()
        .username("RozaKarimova")
        .password("rozaroza")
        .role(UserRole.CLIENT)
        .build();

    Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(user);
    assertEquals(1, violations.size());
    assertEquals(
        "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace",
        violations.iterator().next().getMessage()
    );
  }

  @Test
  void whenAllFieldsAreValid_thenNoValidationErrors() {
    UserRequestDto user = UserRequestDto.builder()
        .username("RozaKarimova")
        .password("Roza2004@")
        .role(UserRole.CLIENT)
        .build();

    Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(user);
    assertEquals(0, violations.size());
  }
}
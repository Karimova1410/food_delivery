package kg.alatoo.food_delivery.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import kg.alatoo.food_delivery.dto.dish.DishRequestDto;
import kg.alatoo.food_delivery.enums.DishCategory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DishRequestDtoTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void whenNameIsTooLong_thenValidationFails() {
    DishRequestDto dish = DishRequestDto.builder()
        .name("a".repeat(101))
        .description("Valid description")
        .price(10.0)
        .category(DishCategory.APPETIZER)
        .restaurantId(1L)
        .build();

    Set<ConstraintViolation<DishRequestDto>> violations = validator.validate(dish);
    assertEquals(1, violations.size());
    assertEquals("Name must be less than 100 characters", violations.iterator().next().getMessage());
  }

  @Test
  void whenDescriptionIsTooLong_thenValidationFails() {
    DishRequestDto dish = DishRequestDto.builder()
        .name("Valid name")
        .description("a".repeat(501))
        .price(10.0)
        .category(DishCategory.APPETIZER)
        .restaurantId(1L)
        .build();

    Set<ConstraintViolation<DishRequestDto>> violations = validator.validate(dish);
    assertEquals(1, violations.size());
    assertEquals("Description must be less than 500 characters", violations.iterator().next().getMessage());
  }

  @Test
  void whenPriceIsNegative_thenValidationFails() {
    DishRequestDto dish = DishRequestDto.builder()
        .name("Valid name")
        .description("Valid description")
        .price(-10.0)
        .category(DishCategory.APPETIZER)
        .restaurantId(1L)
        .build();

    Set<ConstraintViolation<DishRequestDto>> violations = validator.validate(dish);
    assertEquals(1, violations.size());
    assertEquals("Price must be a positive value", violations.iterator().next().getMessage());
  }

  @Test
  void whenRestaurantIdIsNegative_thenValidationFails() {
    DishRequestDto dish = DishRequestDto.builder()
        .name("Valid name")
        .description("Valid description")
        .price(10.0)
        .category(DishCategory.APPETIZER)
        .restaurantId(-1L)
        .build();

    Set<ConstraintViolation<DishRequestDto>> violations = validator.validate(dish);
    assertEquals(1, violations.size());
    assertEquals("Restaurant ID must be a positive value", violations.iterator().next().getMessage());
  }

  @Test
  void whenAllFieldsAreValid_thenNoValidationErrors() {
    DishRequestDto dish = DishRequestDto.builder()
        .name("Valid name")
        .description("Valid description")
        .price(10.0)
        .category(DishCategory.APPETIZER)
        .restaurantId(1L)
        .build();

    Set<ConstraintViolation<DishRequestDto>> violations = validator.validate(dish);
    assertEquals(0, violations.size());
  }
}
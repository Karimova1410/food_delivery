package kg.alatoo.food_delivery.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RestaurantRequestDtoTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void whenNameIsTooLong_thenValidationFails() {
    RestaurantRequestDto restaurant = RestaurantRequestDto.builder()
        .name("a".repeat(101))
        .address("Valid address")
        .dishIds(List.of(1L, 2L))
        .build();

    Set<ConstraintViolation<RestaurantRequestDto>> violations = validator.validate(restaurant);
    assertEquals(1, violations.size());
    assertEquals("Name must be less than 100 characters", violations.iterator().next().getMessage());
  }

  @Test
  void whenAddressIsTooLong_thenValidationFails() {
    RestaurantRequestDto restaurant = RestaurantRequestDto.builder()
        .name("Valid name")
        .address("a".repeat(201))
        .dishIds(List.of(1L, 2L))
        .build();

    Set<ConstraintViolation<RestaurantRequestDto>> violations = validator.validate(restaurant);
    assertEquals(1, violations.size());
    assertEquals("Address must be less than 200 characters", violations.iterator().next().getMessage());
  }

  @Test
  void whenDishIdsContainNegativeValue_thenValidationFails() {
    RestaurantRequestDto restaurant = RestaurantRequestDto.builder()
        .name("Valid name")
        .address("Valid address")
        .dishIds(List.of(1L, -2L))
        .build();

    Set<ConstraintViolation<RestaurantRequestDto>> violations = validator.validate(restaurant);
    assertEquals(1, violations.size());
    assertEquals("Each dish ID must be a positive value", violations.iterator().next().getMessage());
  }

  @Test
  void whenAllFieldsAreValid_thenNoValidationErrors() {
    RestaurantRequestDto restaurant = RestaurantRequestDto.builder()
        .name("Valid name")
        .address("Valid address")
        .dishIds(List.of(1L, 2L))
        .build();

    Set<ConstraintViolation<RestaurantRequestDto>> violations = validator.validate(restaurant);
    assertEquals(0, violations.size());
  }
}
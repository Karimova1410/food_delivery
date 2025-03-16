package kg.alatoo.food_delivery.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import kg.alatoo.food_delivery.dto.order.OrderRequestDto;
import kg.alatoo.food_delivery.enums.OrderStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderRequestDtoTest {

  private static Validator validator;

  @BeforeAll
  static void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void whenClientIdIsNegative_thenValidationFails() {
    OrderRequestDto order = OrderRequestDto.builder()
        .clientId(-1L)
        .restaurantId(1L)
        .dishIds(List.of(1L, 2L))
        .status(OrderStatus.PROCESSING)
        .courierId(1L)
        .build();

    Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(order);
    assertEquals(1, violations.size());
    assertEquals("Client ID must be a positive value", violations.iterator().next().getMessage());
  }

  @Test
  void whenRestaurantIdIsNegative_thenValidationFails() {
    OrderRequestDto order = OrderRequestDto.builder()
        .clientId(1L)
        .restaurantId(-1L)
        .dishIds(List.of(1L, 2L))
        .status(OrderStatus.PROCESSING)
        .courierId(1L)
        .build();

    Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(order);
    assertEquals(1, violations.size());
    assertEquals("Restaurant ID must be a positive value", violations.iterator().next().getMessage());
  }

  @Test
  void whenDishIdsContainNegativeValue_thenValidationFails() {
    OrderRequestDto order = OrderRequestDto.builder()
        .clientId(1L)
        .restaurantId(1L)
        .dishIds(List.of(1L, -2L))
        .status(OrderStatus.PROCESSING)
        .courierId(1L)
        .build();

    Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(order);
    assertEquals(1, violations.size());
    assertEquals("Each dish ID must be a positive value", violations.iterator().next().getMessage());
  }

  @Test
  void whenCourierIdIsNegative_thenValidationFails() {
    OrderRequestDto order = OrderRequestDto.builder()
        .clientId(1L)
        .restaurantId(1L)
        .dishIds(List.of(1L, 2L))
        .status(OrderStatus.PROCESSING)
        .courierId(-1L)
        .build();

    Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(order);
    assertEquals(1, violations.size());
    assertEquals("Courier ID must be a positive value", violations.iterator().next().getMessage());
  }

  @Test
  void whenAllFieldsAreValid_thenNoValidationErrors() {
    OrderRequestDto order = OrderRequestDto.builder()
        .clientId(1L)
        .restaurantId(1L)
        .dishIds(List.of(1L, 2L))
        .status(OrderStatus.PROCESSING)
        .courierId(1L)
        .build();

    Set<ConstraintViolation<OrderRequestDto>> violations = validator.validate(order);
    assertEquals(0, violations.size());
  }
}
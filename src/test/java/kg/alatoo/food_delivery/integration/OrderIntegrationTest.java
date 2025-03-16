package kg.alatoo.food_delivery.integration;

import kg.alatoo.food_delivery.dto.order.OrderRequestDto;
import kg.alatoo.food_delivery.dto.order.OrderResponseDto;
import kg.alatoo.food_delivery.entity.Dish;
import kg.alatoo.food_delivery.entity.Order;
import kg.alatoo.food_delivery.entity.Restaurant;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.enums.OrderStatus;
import kg.alatoo.food_delivery.repository.DishRepository;
import kg.alatoo.food_delivery.repository.OrderRepository;
import kg.alatoo.food_delivery.repository.RestaurantRepository;
import kg.alatoo.food_delivery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private DishRepository dishRepository;

  @Autowired
  private RestaurantRepository restaurantRepository;

  @BeforeEach
  void setUp() {
    orderRepository.deleteAll();
    userRepository.deleteAll();
    restaurantRepository.deleteAll();
  }

  @Test
  void testCreateOrder() {
    User client = userRepository.save(User.builder().username("testClient").build());
    Restaurant restaurant = restaurantRepository.save(
        Restaurant.builder().name("Test Restaurant").build());
    User courier = userRepository.save(User.builder().username("testCourier").build());

    Dish dish1 = dishRepository.save(Dish.builder().name("Dish 1").price(10.99).build());
    Dish dish2 = dishRepository.save(Dish.builder().name("Dish 2").price(15.99).build());

    OrderRequestDto orderRequestDto = new OrderRequestDto(
        client.getId(),
        restaurant.getId(),
        List.of(dish1.getId(), dish2.getId()),
        OrderStatus.PROCESSING,
        courier.getId()
    );

    ResponseEntity<OrderResponseDto> response = restTemplate.postForEntity(
        "/api/orders",
        orderRequestDto,
        OrderResponseDto.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().clientId()).isEqualTo(client.getId());
    assertThat(response.getBody().restaurantId()).isEqualTo(restaurant.getId());
    assertThat(response.getBody().dishIds()).containsExactly(dish1.getId(), dish2.getId());
    assertThat(response.getBody().status()).isEqualTo(OrderStatus.PROCESSING);
    assertThat(response.getBody().courierId()).isEqualTo(courier.getId());

    // Verify the order is saved in the database
    Order savedOrder = orderRepository.findById(response.getBody().id()).orElseThrow();
    assertThat(savedOrder.getClient().getId()).isEqualTo(client.getId());
    assertThat(savedOrder.getRestaurant().getId()).isEqualTo(restaurant.getId());
    assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.PROCESSING);
  }

  @Test
  void testGetOrderById() {
    User client = userRepository.save(User.builder().username("testClient").build());
    Restaurant restaurant = restaurantRepository.save(
        Restaurant.builder().name("Test Restaurant").build());
    User courier = userRepository.save(User.builder().username("testCourier").build());

    Order order = orderRepository.save(Order.builder()
        .client(client)
        .restaurant(restaurant)
        .status(OrderStatus.PROCESSING)
        .courier(courier)
        .build());

    ResponseEntity<OrderResponseDto> response = restTemplate.getForEntity(
        "/api/orders/" + order.getId(),
        OrderResponseDto.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().id()).isEqualTo(order.getId());
    assertThat(response.getBody().clientId()).isEqualTo(client.getId());
    assertThat(response.getBody().restaurantId()).isEqualTo(restaurant.getId());
    assertThat(response.getBody().status()).isEqualTo(OrderStatus.PROCESSING);
    assertThat(response.getBody().courierId()).isEqualTo(courier.getId());
  }

  @Test
  void testUpdateOrder() {
    User client = userRepository.save(User.builder().username("testClient").build());
    Restaurant restaurant = restaurantRepository.save(
        Restaurant.builder().name("Test Restaurant").build());
    User courier = userRepository.save(User.builder().username("testCourier").build());

    Dish dish1 = dishRepository.save(Dish.builder().name("Dish 1").price(10.99).build());
    Dish dish2 = dishRepository.save(Dish.builder().name("Dish 2").price(15.99).build());

    Order order = orderRepository.save(Order.builder()
        .client(client)
        .restaurant(restaurant)
        .status(OrderStatus.PROCESSING)
        .courier(courier)
        .build());

    OrderRequestDto updateRequest = new OrderRequestDto(
        client.getId(),
        restaurant.getId(),
        List.of(dish1.getId(), dish2.getId()),
        OrderStatus.COMPLETED,
        courier.getId()
    );

    restTemplate.put("/api/orders/" + order.getId(), updateRequest);

    Order updatedOrder = orderRepository.findById(order.getId()).orElseThrow();
    assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.COMPLETED);
  }

  @Test
  void testDeleteOrder() {
    User client = userRepository.save(User.builder().username("testClient").build());
    Restaurant restaurant = restaurantRepository.save(
        Restaurant.builder().name("Test Restaurant").build());
    User courier = userRepository.save(User.builder().username("testCourier").build());

    Order order = orderRepository.save(Order.builder()
        .client(client)
        .restaurant(restaurant)
        .status(OrderStatus.PROCESSING)
        .courier(courier)
        .build());

    restTemplate.delete("/api/orders/" + order.getId());

    assertThat(orderRepository.findById(order.getId())).isEmpty();
  }
}
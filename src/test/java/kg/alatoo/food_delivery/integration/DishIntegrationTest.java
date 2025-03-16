package kg.alatoo.food_delivery.integration;

import kg.alatoo.food_delivery.dto.dish.DishRequestDto;
import kg.alatoo.food_delivery.dto.dish.DishResponseDto;
import kg.alatoo.food_delivery.entity.Dish;
import kg.alatoo.food_delivery.entity.Restaurant;
import kg.alatoo.food_delivery.enums.DishCategory;
import kg.alatoo.food_delivery.repository.DishRepository;
import kg.alatoo.food_delivery.repository.OrderRepository;
import kg.alatoo.food_delivery.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DishIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private DishRepository dishRepository;

  @Autowired
  private RestaurantRepository restaurantRepository;
  @Autowired
  private OrderRepository orderRepository;

  @BeforeEach
  void setUp() {
    dishRepository.deleteAll();
    orderRepository.deleteAll();
    restaurantRepository.deleteAll();
  }

  @Test
  void testCreateDish() {
    Restaurant restaurant = restaurantRepository.save(Restaurant.builder()
        .name("Test Restaurant")
        .address("123 Test Street")
        .build());

    DishRequestDto dishRequestDto = new DishRequestDto(
        "Test Dish",
        "Test Description",
        10.99,
        DishCategory.MAIN_COURSE,
        restaurant.getId()
    );

    ResponseEntity<DishResponseDto> response = restTemplate.postForEntity(
        "/api/dishes",
        dishRequestDto,
        DishResponseDto.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().name()).isEqualTo("Test Dish");
    assertThat(response.getBody().description()).isEqualTo("Test Description");
    assertThat(response.getBody().price()).isEqualTo(10.99);
    assertThat(response.getBody().category()).isEqualTo(DishCategory.MAIN_COURSE);
    assertThat(response.getBody().restaurantId()).isEqualTo(restaurant.getId());

    Dish savedDish = dishRepository.findById(response.getBody().id()).orElseThrow();
    assertThat(savedDish.getName()).isEqualTo("Test Dish");
    assertThat(savedDish.getRestaurant().getId()).isEqualTo(restaurant.getId());
  }

  @Test
  void testGetDishById() {
    Restaurant restaurant = restaurantRepository.save(Restaurant.builder()
        .name("Test Restaurant")
        .address("123 Test Street")
        .build());

    Dish dish = dishRepository.save(Dish.builder()
        .name("Test Dish")
        .description("Test Description")
        .price(10.99)
        .category(DishCategory.MAIN_COURSE)
        .restaurant(restaurant)
        .build());

    ResponseEntity<DishResponseDto> response = restTemplate.getForEntity(
        "/api/dishes/" + dish.getId(),
        DishResponseDto.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().id()).isEqualTo(dish.getId());
    assertThat(response.getBody().name()).isEqualTo("Test Dish");
    assertThat(response.getBody().description()).isEqualTo("Test Description");
    assertThat(response.getBody().price()).isEqualTo(10.99);
    assertThat(response.getBody().category()).isEqualTo(DishCategory.MAIN_COURSE);
    assertThat(response.getBody().restaurantId()).isEqualTo(restaurant.getId());
  }

  @Test
  void testUpdateDish() {
    Restaurant restaurant = restaurantRepository.save(Restaurant.builder()
        .name("Test Restaurant")
        .address("123 Test Street")
        .build());

    Dish dish = dishRepository.save(Dish.builder()
        .name("Test Dish")
        .description("Test Description")
        .price(10.99)
        .category(DishCategory.MAIN_COURSE)
        .restaurant(restaurant)
        .build());

    DishRequestDto updateRequest = new DishRequestDto(
        "Updated Dish",
        "Updated Description",
        15.99,
        DishCategory.APPETIZER,
        restaurant.getId()
    );

    restTemplate.put("/api/dishes/" + dish.getId(), updateRequest);

    Dish updatedDish = dishRepository.findById(dish.getId()).orElseThrow();
    assertThat(updatedDish.getName()).isEqualTo("Updated Dish");
    assertThat(updatedDish.getDescription()).isEqualTo("Updated Description");
    assertThat(updatedDish.getPrice()).isEqualTo(15.99);
    assertThat(updatedDish.getCategory()).isEqualTo(DishCategory.APPETIZER);
  }

  @Test
  void testDeleteDish() {
    Restaurant restaurant = restaurantRepository.save(Restaurant.builder()
        .name("Test Restaurant")
        .address("123 Test Street")
        .build());

    Dish dish = dishRepository.save(Dish.builder()
        .name("Test Dish")
        .description("Test Description")
        .price(10.99)
        .category(DishCategory.MAIN_COURSE)
        .restaurant(restaurant)
        .build());

    restTemplate.delete("/api/dishes/" + dish.getId());

    assertThat(dishRepository.findById(dish.getId())).isEmpty();
  }
}
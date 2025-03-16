package kg.alatoo.food_delivery.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

import java.util.List;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantRequestDto;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantResponseDto;
import kg.alatoo.food_delivery.entity.Restaurant;
import kg.alatoo.food_delivery.repository.DishRepository;
import kg.alatoo.food_delivery.repository.OrderRepository;
import kg.alatoo.food_delivery.repository.RestaurantRepository;
import kg.alatoo.food_delivery.service.RestaurantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestaurantIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private RestaurantRepository restaurantRepository;

  @Autowired
  private OrderRepository orderRepository;


  @BeforeEach
  void setUp() {
    orderRepository.deleteAll();
    restaurantRepository.deleteAll();
  }

  @Test
  void testCreateRestaurant() {
    RestaurantRequestDto requestDto = new RestaurantRequestDto("Sushi Place", "Chui 132", List.of(1L, 2L));
    ResponseEntity<RestaurantResponseDto> response = restTemplate.postForEntity("/api/restaurants", requestDto, RestaurantResponseDto.class);

    assertThat(response.getStatusCode()).isEqualTo(CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().name()).isEqualTo("Sushi Place");
  }

  @Test
  void testGetRestaurantById() {
    Restaurant restaurant = new Restaurant();
    restaurant.setName("Sushi Place");
    restaurant.setAddress("Chui 132");
    Restaurant savedRestaurant = restaurantRepository.save(restaurant);

    ResponseEntity<RestaurantResponseDto> response = restTemplate.getForEntity("/api/restaurants/" + savedRestaurant.getId(), RestaurantResponseDto.class);

    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().name()).isEqualTo("Sushi Place");
  }

  @Test
  void testUpdateRestaurant() {
    Restaurant restaurant = new Restaurant();
    restaurant.setName("Sushi Place");
    restaurant.setAddress("Chui 132");
    Restaurant savedRestaurant = restaurantRepository.save(restaurant);

    RestaurantRequestDto updateDto = new RestaurantRequestDto("Updated Sushi Place", "Chui 132", List.of());
    HttpEntity<RestaurantRequestDto> requestEntity = new HttpEntity<>(updateDto);
    ResponseEntity<RestaurantResponseDto> response = restTemplate.exchange("/api/restaurants/" + savedRestaurant.getId(), HttpMethod.PUT, requestEntity, RestaurantResponseDto.class);

    assertThat(response.getStatusCode()).isEqualTo(OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().name()).isEqualTo("Updated Sushi Place");
  }


}

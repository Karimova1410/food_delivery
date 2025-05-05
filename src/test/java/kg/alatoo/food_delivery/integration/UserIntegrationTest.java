package kg.alatoo.food_delivery.integration;

import kg.alatoo.food_delivery.dto.user.UserRequestDto;
import kg.alatoo.food_delivery.dto.user.UserResponseDto;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.enums.Role;
import kg.alatoo.food_delivery.repository.DishRepository;
import kg.alatoo.food_delivery.repository.OrderRepository;
import kg.alatoo.food_delivery.repository.RestaurantRepository;
import kg.alatoo.food_delivery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private UserRepository userRepository;
  @Autowired
  private DishRepository dishRepository;
  @Autowired
  private OrderRepository orderRepository;
  @Autowired
  private RestaurantRepository restaurantRepository;

  @BeforeEach
  void setUp() {
    dishRepository.deleteAll();
    orderRepository.deleteAll();
    restaurantRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void testCreateUser() {
    UserRequestDto userRequestDto = new UserRequestDto(
        "roza",
        "P@assword123",
        Role.CLIENT
    );

    ResponseEntity<UserResponseDto> response = restTemplate.postForEntity(
        "/api/users",
        userRequestDto,
        UserResponseDto.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().username()).isEqualTo("roza");
    assertThat(response.getBody().role()).isEqualTo(Role.CLIENT);

    User savedUser = userRepository.findById(response.getBody().id()).orElseThrow();
    assertThat(savedUser.getUsername()).isEqualTo("roza");
    assertThat(savedUser.getRole()).isEqualTo(Role.CLIENT);
  }

  @Test
  void testGetUserById() {
    User user = userRepository.save(User.builder()
        .username("roza")
        .password("P@assword123")
        .role(Role.CLIENT)
        .build());

    ResponseEntity<UserResponseDto> response = restTemplate.getForEntity(
        "/api/users/" + user.getId(),
        UserResponseDto.class
    );

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().id()).isEqualTo(user.getId());
    assertThat(response.getBody().username()).isEqualTo("roza");
    assertThat(response.getBody().role()).isEqualTo(Role.CLIENT);
  }

  @Test
  void testUpdateUser() {
    User user = userRepository.save(User.builder()
        .username("roza")
        .password("P@assword123")
        .role(Role.CLIENT)
        .build());
    System.out.println(user);

    UserRequestDto updateRequest = new UserRequestDto(
        "updatedRoza",
        "NewPassword123@",
        Role.ADMIN
    );

    ResponseEntity<Void> response = restTemplate.exchange(
        "/api/users/" + user.getId(),
        HttpMethod.PUT,
        new HttpEntity<>(updateRequest),
        Void.class
    );

    User updatedUser = userRepository.findById(user.getId()).orElseThrow();
    assertThat(updatedUser.getUsername()).isEqualTo("updatedRoza");
    assertThat(updatedUser.getRole()).isEqualTo(Role.ADMIN);
  }

  @Test
  void testDeleteUser() {
    User user = userRepository.save(User.builder()
        .username("roza")
        .password("P@assword123")
        .role(Role.CLIENT)
        .build());

    restTemplate.delete("/api/users/" + user.getId());

    assertThat(userRepository.findById(user.getId())).isEmpty();
  }
}
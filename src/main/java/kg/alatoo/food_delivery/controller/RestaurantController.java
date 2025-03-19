package kg.alatoo.food_delivery.controller;

import jakarta.validation.Valid;
import java.util.List;
import kg.alatoo.food_delivery.controller.swagger.RestaurantControllerSwagger;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantRequestDto;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantResponseDto;
import kg.alatoo.food_delivery.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController implements RestaurantControllerSwagger {

  private final RestaurantService restaurantService;

  public RestaurantController(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }

  @PostMapping
  public ResponseEntity<RestaurantResponseDto> createRestaurant(@Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
    RestaurantResponseDto restaurantResponseDto = restaurantService.createRestaurant(restaurantRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(restaurantResponseDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<RestaurantResponseDto> getRestaurantById(@PathVariable Long id) {
    RestaurantResponseDto restaurantResponseDto = restaurantService.getRestaurantById(id);
    return ResponseEntity.ok(restaurantResponseDto);
  }

  @GetMapping
  public ResponseEntity<List<RestaurantResponseDto>> getAllRestaurants() {
    List<RestaurantResponseDto> restaurants = restaurantService.getAllRestaurants();
    return ResponseEntity.ok(restaurants);
  }

  @PutMapping("/{id}")
  public ResponseEntity<RestaurantResponseDto> updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
    RestaurantResponseDto restaurantResponseDto = restaurantService.updateRestaurant(id, restaurantRequestDto);
    return ResponseEntity.ok(restaurantResponseDto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
    restaurantService.deleteRestaurant(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/{id}")
  public ResponseEntity<RestaurantResponseDto> patchRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
    RestaurantResponseDto restaurantResponseDto = restaurantService.patchRestaurant(id, restaurantRequestDto);
    return ResponseEntity.ok(restaurantResponseDto);
  }
}
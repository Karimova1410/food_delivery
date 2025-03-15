package kg.alatoo.food_delivery.service;

import jakarta.validation.Valid;
import java.util.List;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantRequestDto;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantResponseDto;

public interface RestaurantService {

  RestaurantResponseDto createRestaurant(@Valid RestaurantRequestDto restaurantRequestDto);

  RestaurantResponseDto getRestaurantById(Long id);

  List<RestaurantResponseDto> getAllRestaurants();

  RestaurantResponseDto updateRestaurant(Long id, @Valid RestaurantRequestDto restaurantRequestDto);

  void deleteRestaurant(Long id);

  RestaurantResponseDto patchRestaurant(Long id, @Valid RestaurantRequestDto restaurantRequestDto);
}

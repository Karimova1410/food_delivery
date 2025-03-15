package kg.alatoo.food_delivery.mapper;

import kg.alatoo.food_delivery.dto.restaurant.RestaurantRequestDto;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantResponseDto;
import kg.alatoo.food_delivery.entity.Restaurant;

public interface RestaurantMapper {

  Restaurant toEntity(RestaurantRequestDto restaurantRequestDto);

  RestaurantResponseDto toDto(Restaurant restaurant);
}

package kg.alatoo.food_delivery.mapper.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantRequestDto;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantResponseDto;
import kg.alatoo.food_delivery.entity.Dish;
import kg.alatoo.food_delivery.entity.Restaurant;
import kg.alatoo.food_delivery.mapper.RestaurantMapper;
import kg.alatoo.food_delivery.repository.DishRepository;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapperImpl implements RestaurantMapper {

  private final DishRepository dishRepository;

  public RestaurantMapperImpl(DishRepository dishRepository) {
    this.dishRepository = dishRepository;
  }

  @Override
  public Restaurant toEntity(RestaurantRequestDto restaurantRequestDto) {
    Restaurant restaurant = Restaurant.builder()
        .name(restaurantRequestDto.name())
        .address(restaurantRequestDto.address())
        .build();

    List<Dish> dishes = restaurantRequestDto.dishIds().stream()
        .map(dishId -> dishRepository.findById(dishId))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .peek(dish -> dish.setRestaurant(restaurant)) // Устанавливаем связь
        .collect(Collectors.toList());

    restaurant.setMenu(dishes);
    return restaurant;
  }
//  public Restaurant toEntity(RestaurantRequestDto restaurantRequestDto) {
//    Restaurant restaurant = Restaurant.builder()
//        .name(restaurantRequestDto.name())
//        .address(restaurantRequestDto.address())
////        .menu(restaurantRequestDto.dishIds().stream()
////            .map(dishId -> dishRepository.findById(dishId)
////                .orElseThrow(() -> new RuntimeException("Dish not found")))
////            .collect(Collectors.toList()))
//        .build();
//
////
//    List<Dish> dishes = restaurantRequestDto.dishIds().stream()
//        .map(dishId -> dishRepository.findById(dishId)
//            .filter(Optional::isPresent)
//            .map(Optional::get)
//            .peek(dish -> dish.setRestaurant(restaurant)) // Устанавливаем связь
//            .collect(Collectors.toList());
//
//    restaurant.setMenu(dishes);
//    return restaurant;
//  }

  @Override
  public RestaurantResponseDto toDto(Restaurant restaurant) {
    return RestaurantResponseDto.builder()
        .id(restaurant.getId())
        .name(restaurant.getName())
        .address(restaurant.getAddress())
        .dishIds(restaurant.getMenu().stream()
            .map(Dish::getId)
            .collect(Collectors.toList()))
        .build();
  }
}

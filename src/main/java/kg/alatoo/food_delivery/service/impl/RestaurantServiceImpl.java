package kg.alatoo.food_delivery.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantRequestDto;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantResponseDto;
import kg.alatoo.food_delivery.entity.Restaurant;
import kg.alatoo.food_delivery.mapper.RestaurantMapper;
import kg.alatoo.food_delivery.repository.DishRepository;
import kg.alatoo.food_delivery.repository.RestaurantRepository;
import kg.alatoo.food_delivery.service.RestaurantService;
import org.springframework.stereotype.Service;
@Service
public class RestaurantServiceImpl implements RestaurantService {

  private final RestaurantRepository restaurantRepository;
  private final DishRepository dishRepository;
  private final RestaurantMapper restaurantMapper;

  public RestaurantServiceImpl(RestaurantRepository restaurantRepository, DishRepository dishRepository, RestaurantMapper restaurantMapper) {
    this.restaurantRepository = restaurantRepository;
    this.dishRepository = dishRepository;
    this.restaurantMapper = restaurantMapper;
  }

  @Override
  public RestaurantResponseDto createRestaurant(RestaurantRequestDto restaurantRequestDto) {
    Restaurant restaurant = restaurantMapper.toEntity(restaurantRequestDto);
    Restaurant savedRestaurant = restaurantRepository.save(restaurant);
    return restaurantMapper.toDto(savedRestaurant);
  }

  @Override
  public RestaurantResponseDto getRestaurantById(Long id) {
    Restaurant restaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    return restaurantMapper.toDto(restaurant);
  }

  @Override
  public List<RestaurantResponseDto> getAllRestaurants() {
    return restaurantRepository.findAll().stream()
        .map(restaurantMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public RestaurantResponseDto updateRestaurant(Long id, RestaurantRequestDto restaurantRequestDto) {
    Restaurant existingRestaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Restaurant not found"));

    Restaurant updatedRestaurant = restaurantMapper.toEntity(restaurantRequestDto);
    updatedRestaurant.setId(existingRestaurant.getId()); // Ensure the ID remains the same

    Restaurant savedRestaurant = restaurantRepository.save(updatedRestaurant);
    return restaurantMapper.toDto(savedRestaurant);
  }

  @Override
  public void deleteRestaurant(Long id) {
    if (!restaurantRepository.existsById(id)) {
      throw new RuntimeException("Restaurant not found");
    }
    restaurantRepository.deleteById(id);
  }

  @Override
  public RestaurantResponseDto patchRestaurant(Long id, RestaurantRequestDto restaurantRequestDto) {
    Restaurant existingRestaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Restaurant not found"));

    if (restaurantRequestDto.name() != null) {
      existingRestaurant.setName(restaurantRequestDto.name());
    }
    if (restaurantRequestDto.address() != null) {
      existingRestaurant.setAddress(restaurantRequestDto.address());
    }
    if (restaurantRequestDto.dishIds() != null && !restaurantRequestDto.dishIds().isEmpty()) {
      existingRestaurant.setMenu(restaurantRequestDto.dishIds().stream()
          .map(dishId -> dishRepository.findById(dishId)
              .orElseThrow(() -> new RuntimeException("Dish not found")))
          .collect(Collectors.toList()));
    }

    Restaurant savedRestaurant = restaurantRepository.save(existingRestaurant);
    return restaurantMapper.toDto(savedRestaurant);
  }
}
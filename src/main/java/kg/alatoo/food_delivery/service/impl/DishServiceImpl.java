package kg.alatoo.food_delivery.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import kg.alatoo.food_delivery.dto.dish.DishRequestDto;
import kg.alatoo.food_delivery.dto.dish.DishResponseDto;
import kg.alatoo.food_delivery.entity.Dish;
import kg.alatoo.food_delivery.entity.Restaurant;
import kg.alatoo.food_delivery.mapper.mapstruct.DishMapstructMapper;
import kg.alatoo.food_delivery.repository.DishRepository;
import kg.alatoo.food_delivery.repository.RestaurantRepository;
import kg.alatoo.food_delivery.service.DishService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

  private final DishRepository dishRepository;
  private final RestaurantRepository restaurantRepository;
  private final DishMapstructMapper dishMapper;

  @Override
  public List<DishResponseDto> findAll() {
    List<Dish> dishes = dishRepository.findAll();
    return dishes.stream()
        .map(dishMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public DishResponseDto findById(Long id) {
    Dish dish = dishRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Dish not found with id: " + id));
    return dishMapper.toDto(dish);
  }

  @Override
  public DishResponseDto createDish(DishRequestDto dishRequestDto) {
    Dish dish = dishMapper.toEntity(dishRequestDto);

    Restaurant restaurant = restaurantRepository.findById(dishRequestDto.restaurantId())
        .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + dishRequestDto.restaurantId()));
    dish.setRestaurant(restaurant);

    Dish savedDish = dishRepository.save(dish);
    return dishMapper.toDto(savedDish);
  }

  @Override
  public DishResponseDto updateDish(Long id, DishRequestDto dishRequestDto) {
    Dish existingDish = dishRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Dish not found with id: " + id));

    existingDish.setName(dishRequestDto.name());
    existingDish.setDescription(dishRequestDto.description());
    existingDish.setPrice(dishRequestDto.price());
    existingDish.setCategory(dishRequestDto.category());

    if (!existingDish.getRestaurant().getId().equals(dishRequestDto.restaurantId())) {
      Restaurant restaurant = restaurantRepository.findById(dishRequestDto.restaurantId())
          .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + dishRequestDto.restaurantId()));
      existingDish.setRestaurant(restaurant);
    }

    Dish updatedDish = dishRepository.save(existingDish);
    return dishMapper.toDto(updatedDish);
  }

  @Override
  public void deleteDish(Long id) {
    if (!dishRepository.existsById(id)) {
      throw new RuntimeException("Dish not found with id: " + id);
    }
    dishRepository.deleteById(id);
  }
}
package kg.alatoo.food_delivery.service;

import java.util.List;
import kg.alatoo.food_delivery.dto.dish.DishRequestDto;
import kg.alatoo.food_delivery.dto.dish.DishResponseDto;

public interface DishService {

  List<DishResponseDto> findAll();

  DishResponseDto findById(Long id);

  DishResponseDto createDish(DishRequestDto dishRequestDto);

  DishResponseDto updateDish(Long id, DishRequestDto dishRequestDto);

  void deleteDish(Long id);

  DishResponseDto patchDish(Long id, DishRequestDto dishRequestDto);
}

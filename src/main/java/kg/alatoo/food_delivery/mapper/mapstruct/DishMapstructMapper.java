package kg.alatoo.food_delivery.mapper.mapstruct;

import kg.alatoo.food_delivery.dto.dish.DishRequestDto;
import kg.alatoo.food_delivery.dto.dish.DishResponseDto;
import kg.alatoo.food_delivery.entity.Dish;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DishMapstructMapper {

  @Mapping(target = "restaurant", ignore = true)
  Dish toEntity(DishRequestDto dishRequestDto);

  DishResponseDto toDto(Dish dish);
}

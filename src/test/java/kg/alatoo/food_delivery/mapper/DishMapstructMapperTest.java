package kg.alatoo.food_delivery.mapper;

import kg.alatoo.food_delivery.dto.dish.DishRequestDto;
import kg.alatoo.food_delivery.dto.dish.DishResponseDto;
import kg.alatoo.food_delivery.entity.Dish;
import kg.alatoo.food_delivery.entity.Restaurant;
import kg.alatoo.food_delivery.enums.DishCategory;
import kg.alatoo.food_delivery.mapper.mapstruct.DishMapstructMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class DishMapstructMapperTest {

  private final DishMapstructMapper dishMapstructMapper = Mappers.getMapper(DishMapstructMapper.class);

  @Test
  void testToEntity() {
    DishRequestDto dishRequestDto = new DishRequestDto("Plov", "Plov Description", 10.99, DishCategory.MAIN_COURSE, 1L);

    Dish dish = dishMapstructMapper.toEntity(dishRequestDto);

    assertThat(dish).isNotNull();
    assertThat(dish.getName()).isEqualTo(dishRequestDto.name());
    assertThat(dish.getDescription()).isEqualTo(dishRequestDto.description());
    assertThat(dish.getPrice()).isEqualTo(dishRequestDto.price());
    assertThat(dish.getCategory()).isEqualTo(dishRequestDto.category());
    assertThat(dish.getRestaurant()).isNull();
  }

  @Test
  void testToDto() {
    Restaurant restaurant = Restaurant.builder()
        .id(1L)
        .name("Alatoo")
        .address("Ankara 8/2")
        .build();

    Dish dish = Dish.builder()
        .id(1L)
        .name("Plov")
        .description("Plov Description")
        .price(10.99)
        .category(DishCategory.MAIN_COURSE)
        .restaurant(restaurant)
        .build();

    DishResponseDto dishResponseDto = dishMapstructMapper.toDto(dish);

    assertThat(dishResponseDto).isNotNull();
    assertThat(dishResponseDto.id()).isEqualTo(dish.getId());
    assertThat(dishResponseDto.name()).isEqualTo(dish.getName());
    assertThat(dishResponseDto.description()).isEqualTo(dish.getDescription());
    assertThat(dishResponseDto.price()).isEqualTo(dish.getPrice());
    assertThat(dishResponseDto.category()).isEqualTo(dish.getCategory());
    assertThat(dishResponseDto.restaurantId()).isEqualTo(restaurant.getId());
  }
}
package kg.alatoo.food_delivery.mapper;

import kg.alatoo.food_delivery.dto.restaurant.RestaurantRequestDto;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantResponseDto;
import kg.alatoo.food_delivery.entity.Dish;
import kg.alatoo.food_delivery.entity.Restaurant;
import kg.alatoo.food_delivery.mapper.impl.RestaurantMapperImpl;
import kg.alatoo.food_delivery.repository.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestaurantMapperImplTest {

  @Mock
  private DishRepository dishRepository;

  @InjectMocks
  private RestaurantMapperImpl restaurantMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testToEntity() {
    String name = "Ala-too";
    String address = "Ankara 8/1";
    List<Long> dishIds = Arrays.asList(1L, 2L);

    Dish dish1 = new Dish();
    dish1.setId(1L);

    Dish dish2 = new Dish();
    dish2.setId(2L);

    RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto(name, address, dishIds);

    when(dishRepository.findById(1L)).thenReturn(Optional.of(dish1));
    when(dishRepository.findById(2L)).thenReturn(Optional.of(dish2));


    Restaurant restaurant = restaurantMapper.toEntity(restaurantRequestDto);


    assertNotNull(restaurant);
    assertEquals(name, restaurant.getName());
    assertEquals(address, restaurant.getAddress());
    assertEquals(2, restaurant.getMenu().size());
    assertEquals(dish1, restaurant.getMenu().get(0));
    assertEquals(dish2, restaurant.getMenu().get(1));
    //bidirectional
    assertEquals(restaurant, dish1.getRestaurant());
    assertEquals(restaurant, dish2.getRestaurant());

    verify(dishRepository, times(1)).findById(1L);
    verify(dishRepository, times(1)).findById(2L);
  }

  @Test
  void testToEntity_WithInvalidDishId() {
    String name = "Ala-too";
    String address = "Ankara 8/1";
    List<Long> dishIds = Arrays.asList(1L, 99L);

    Dish dish1 = new Dish();
    dish1.setId(1L);

    RestaurantRequestDto restaurantRequestDto = new RestaurantRequestDto(name, address, dishIds);

    when(dishRepository.findById(1L)).thenReturn(Optional.of(dish1));
    when(dishRepository.findById(99L)).thenReturn(Optional.empty());

    Restaurant restaurant = restaurantMapper.toEntity(restaurantRequestDto);

    assertNotNull(restaurant);
    assertEquals(name, restaurant.getName());
    assertEquals(address, restaurant.getAddress());
    assertEquals(1, restaurant.getMenu().size());
    assertEquals(dish1, restaurant.getMenu().get(0));
    assertEquals(restaurant, dish1.getRestaurant());

    verify(dishRepository, times(1)).findById(1L);
    verify(dishRepository, times(1)).findById(99L);
  }

  @Test
  void testToDto() {
    Long id = 1L;
    String name = "Ala-too";
    String address = "Ankara 8/1";
    List<Long> dishIds = Arrays.asList(1L, 2L);

    Dish dish1 = new Dish();
    dish1.setId(1L);

    Dish dish2 = new Dish();
    dish2.setId(2L);

    Restaurant restaurant = new Restaurant();
    restaurant.setId(id);
    restaurant.setName(name);
    restaurant.setAddress(address);
    restaurant.setMenu(Arrays.asList(dish1, dish2));


    RestaurantResponseDto restaurantResponseDto = restaurantMapper.toDto(restaurant);


    assertNotNull(restaurantResponseDto);
    assertEquals(id, restaurantResponseDto.id());
    assertEquals(name, restaurantResponseDto.name());
    assertEquals(address, restaurantResponseDto.address());
    assertEquals(dishIds, restaurantResponseDto.dishIds());
  }
}
package kg.alatoo.food_delivery.mapper;

import kg.alatoo.food_delivery.dto.order.OrderRequestDto;
import kg.alatoo.food_delivery.dto.order.OrderResponseDto;
import kg.alatoo.food_delivery.entity.Dish;
import kg.alatoo.food_delivery.entity.Order;
import kg.alatoo.food_delivery.entity.Restaurant;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.enums.OrderStatus;
import kg.alatoo.food_delivery.mapper.impl.OrderMapperImpl;
import kg.alatoo.food_delivery.repository.DishRepository;
import kg.alatoo.food_delivery.repository.RestaurantRepository;
import kg.alatoo.food_delivery.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderMapperImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private DishRepository dishRepository;

  @Mock
  private RestaurantRepository restaurantRepository;

  @InjectMocks
  private OrderMapperImpl orderMapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testToEntity() {
    Long clientId = 1L;
    Long restaurantId = 2L;
    List<Long> dishIds = Arrays.asList(3L, 4L);
    Long courierId = 5L;
    OrderStatus status = OrderStatus.COMPLETED;

    User client = new User();
    client.setId(clientId);

    Restaurant restaurant = new Restaurant();
    restaurant.setId(restaurantId);

    Dish dish1 = new Dish();
    dish1.setId(3L);

    Dish dish2 = new Dish();
    dish2.setId(4L);

    User courier = new User();
    courier.setId(courierId);

    OrderRequestDto orderRequestDto = new OrderRequestDto(clientId, restaurantId, dishIds, status, courierId);

    when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
    when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
    when(dishRepository.findById(3L)).thenReturn(Optional.of(dish1));
    when(dishRepository.findById(4L)).thenReturn(Optional.of(dish2));
    when(userRepository.findById(courierId)).thenReturn(Optional.of(courier));


    Order order = orderMapper.toEntity(orderRequestDto);


    assertNotNull(order);
    assertEquals(clientId, order.getClient().getId());
    assertEquals(restaurantId, order.getRestaurant().getId());
    assertEquals(2, order.getDishes().size());
    assertEquals(status, order.getStatus());
    assertEquals(courierId, order.getCourier().getId());

    verify(userRepository, times(1)).findById(clientId);
    verify(restaurantRepository, times(1)).findById(restaurantId);
    verify(dishRepository, times(1)).findById(3L);
    verify(dishRepository, times(1)).findById(4L);
    verify(userRepository, times(1)).findById(courierId);
  }

  @Test
  void testToDto() {
    Long orderId = 1L;
    Long clientId = 2L;
    Long restaurantId = 3L;
    List<Long> dishIds = Arrays.asList(4L, 5L);
    OrderStatus status = OrderStatus.COMPLETED;
    Long courierId = 6L;

    User client = new User();
    client.setId(clientId);

    Restaurant restaurant = new Restaurant();
    restaurant.setId(restaurantId);

    Dish dish1 = new Dish();
    dish1.setId(4L);

    Dish dish2 = new Dish();
    dish2.setId(5L);

    User courier = new User();
    courier.setId(courierId);

    Order order = new Order();
    order.setId(orderId);
    order.setClient(client);
    order.setRestaurant(restaurant);
    order.setDishes(Arrays.asList(dish1, dish2));
    order.setStatus(status);
    order.setCourier(courier);


    OrderResponseDto orderResponseDto = orderMapper.toDto(order);


    assertNotNull(orderResponseDto);
    assertEquals(orderId, orderResponseDto.id());
    assertEquals(clientId, orderResponseDto.clientId());
    assertEquals(restaurantId, orderResponseDto.restaurantId());
    assertEquals(dishIds, orderResponseDto.dishIds());
    assertEquals(status, orderResponseDto.status());
    assertEquals(courierId, orderResponseDto.courierId());
  }
}
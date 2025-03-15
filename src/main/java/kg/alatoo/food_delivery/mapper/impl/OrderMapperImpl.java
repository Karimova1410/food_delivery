package kg.alatoo.food_delivery.mapper.impl;

import java.util.stream.Collectors;
import kg.alatoo.food_delivery.dto.order.OrderRequestDto;
import kg.alatoo.food_delivery.dto.order.OrderResponseDto;
import kg.alatoo.food_delivery.entity.Dish;
import kg.alatoo.food_delivery.entity.Order;
import kg.alatoo.food_delivery.mapper.OrderMapper;
import kg.alatoo.food_delivery.repository.DishRepository;
import kg.alatoo.food_delivery.repository.RestaurantRepository;
import kg.alatoo.food_delivery.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderMapperImpl implements OrderMapper {
  private final UserRepository userRepository;
  private final DishRepository dishRepository;
  private final RestaurantRepository restaurantRepository;

  public OrderMapperImpl(UserRepository userRepository, DishRepository dishRepository,
      RestaurantRepository restaurantRepository) {
    this.userRepository = userRepository;
    this.dishRepository = dishRepository;
    this.restaurantRepository = restaurantRepository;
  }

  @Override
  public Order toEntity(OrderRequestDto orderRequestDto) {
    return Order.builder()
        .client(userRepository.findById(orderRequestDto.clientId())
            .orElseThrow(() -> new RuntimeException("Client was not found")))
        .restaurant(restaurantRepository.findById(orderRequestDto.restaurantId())
            .orElseThrow(() -> new RuntimeException("Restaurant was not found")))
        .dishes(orderRequestDto.dishIds().stream()
            .map(dishId -> dishRepository.findById(dishId)
                .orElseThrow(() -> new RuntimeException("Dishwas not found")))
            .collect(Collectors.toList()))
        .status(orderRequestDto.status())
        .courier(userRepository.findById(orderRequestDto.courierId())
            .orElseThrow(() -> new RuntimeException("Courier was not found")))
        .build();
  }

  @Override
  public OrderResponseDto toDto(Order order) {
    return OrderResponseDto.builder()
        .id(order.getId())
        .clientId(order.getClient().getId())
        .restaurantId(order.getRestaurant().getId())
        .dishIds(order.getDishes().stream()
            .map(Dish::getId)
            .collect(Collectors.toList()))
        .status(order.getStatus())
        .courierId(order.getCourier().getId())
        .build();
  }
}

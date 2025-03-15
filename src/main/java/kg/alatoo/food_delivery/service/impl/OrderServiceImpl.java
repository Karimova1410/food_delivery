package kg.alatoo.food_delivery.service.impl;

import java.util.List;
import java.util.stream.Collectors;
import kg.alatoo.food_delivery.dto.order.OrderRequestDto;
import kg.alatoo.food_delivery.dto.order.OrderResponseDto;
import kg.alatoo.food_delivery.entity.Order;
import kg.alatoo.food_delivery.mapper.OrderMapper;
import kg.alatoo.food_delivery.repository.DishRepository;
import kg.alatoo.food_delivery.repository.OrderRepository;
import kg.alatoo.food_delivery.repository.RestaurantRepository;
import kg.alatoo.food_delivery.repository.UserRepository;
import kg.alatoo.food_delivery.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderMapper orderMapper;
  private final UserRepository userRepository;
  private final DishRepository dishRepository;
  private final RestaurantRepository restaurantRepository;

  public OrderServiceImpl(OrderRepository orderRepository, OrderMapper orderMapper,
      UserRepository userRepository, DishRepository dishRepository,
      RestaurantRepository restaurantRepository) {
    this.orderRepository = orderRepository;
    this.orderMapper = orderMapper;
    this.userRepository = userRepository;
    this.dishRepository = dishRepository;
    this.restaurantRepository = restaurantRepository;
  }

  @Override
  public List<OrderResponseDto> findAll() {
    return orderRepository.findAll().stream()
        .map(orderMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public OrderResponseDto findById(Long id) {
    Order order = orderRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Order not found"));
    return orderMapper.toDto(order);  }

  public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
    Order order = orderMapper.toEntity(orderRequestDto);
    Order savedOrder = orderRepository.save(order);
    return orderMapper.toDto(savedOrder);
  }


  public OrderResponseDto updateOrder(Long id, OrderRequestDto orderRequestDto) {
    Order existingOrder = orderRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Order not found"));

    Order updatedOrder = orderMapper.toEntity(orderRequestDto);
    updatedOrder.setId(existingOrder.getId());

    Order savedOrder = orderRepository.save(updatedOrder);
    return orderMapper.toDto(savedOrder);
  }

  @Override
  public OrderResponseDto patchOrder(Long id, OrderRequestDto orderRequestDto) {
    Order existingOrder = orderRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Order not found"));

    if (orderRequestDto.clientId() != null) {
      existingOrder.setClient(userRepository.findById(orderRequestDto.clientId())
          .orElseThrow(() -> new RuntimeException("Client not found")));
    }
    if (orderRequestDto.restaurantId() != null) {
      existingOrder.setRestaurant(restaurantRepository.findById(orderRequestDto.restaurantId())
          .orElseThrow(() -> new RuntimeException("Restaurant not found")));
    }
    if (orderRequestDto.dishIds() != null && !orderRequestDto.dishIds().isEmpty()) {
      existingOrder.setDishes(orderRequestDto.dishIds().stream()
          .map(dishId -> dishRepository.findById(dishId)
              .orElseThrow(() -> new RuntimeException("Dish not found")))
          .collect(Collectors.toList()));
    }
    if (orderRequestDto.status() != null) {
      existingOrder.setStatus(orderRequestDto.status());
    }
    if (orderRequestDto.courierId() != null) {
      existingOrder.setCourier(userRepository.findById(orderRequestDto.courierId())
          .orElseThrow(() -> new RuntimeException("Courier not found")));
    }

    Order updatedOrder = orderRepository.save(existingOrder);
    return orderMapper.toDto(updatedOrder);
  }

  public void deleteOrder(Long id) {
    if (!orderRepository.existsById(id)) {
      throw new RuntimeException("Order not found");
    }
    orderRepository.deleteById(id);
  }
}

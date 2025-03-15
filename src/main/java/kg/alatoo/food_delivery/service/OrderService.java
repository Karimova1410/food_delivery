package kg.alatoo.food_delivery.service;

import java.util.List;
import kg.alatoo.food_delivery.dto.order.OrderRequestDto;
import kg.alatoo.food_delivery.dto.order.OrderResponseDto;

public interface OrderService {

  List<OrderResponseDto> findAll();

  OrderResponseDto findById(Long id);

  OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

  OrderResponseDto updateOrder(Long id, OrderRequestDto orderRequestDto);

  OrderResponseDto patchOrder(Long id, OrderRequestDto orderRequestDto);

  void deleteOrder(Long id);
}

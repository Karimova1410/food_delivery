package kg.alatoo.food_delivery.mapper;

import kg.alatoo.food_delivery.dto.order.OrderRequestDto;
import kg.alatoo.food_delivery.dto.order.OrderResponseDto;
import kg.alatoo.food_delivery.entity.Order;

public interface OrderMapper {

  Order toEntity(OrderRequestDto orderRequestDto);

  OrderResponseDto toDto(Order order);
}

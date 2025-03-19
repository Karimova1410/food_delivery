package kg.alatoo.food_delivery.controller;

import kg.alatoo.food_delivery.controller.swagger.OrderControllerSwagger;
import kg.alatoo.food_delivery.dto.order.OrderRequestDto;
import kg.alatoo.food_delivery.dto.order.OrderResponseDto;
import kg.alatoo.food_delivery.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController implements OrderControllerSwagger {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping
  public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
    List<OrderResponseDto> orders = orderService.findAll();
    return new ResponseEntity<>(orders, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
    OrderResponseDto order = orderService.findById(id);
    return new ResponseEntity<>(order, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
    OrderResponseDto createdOrder = orderService.createOrder(orderRequestDto);
    return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<OrderResponseDto> updateOrder(
      @PathVariable Long id,
      @RequestBody OrderRequestDto orderRequestDto) {
    OrderResponseDto updatedOrder = orderService.updateOrder(id, orderRequestDto);
    return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<OrderResponseDto> patchOrder(
      @PathVariable Long id,
      @RequestBody OrderRequestDto orderRequestDto) {
    OrderResponseDto patchedOrder = orderService.patchOrder(id, orderRequestDto);
    return new ResponseEntity<>(patchedOrder, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }
}
package kg.alatoo.food_delivery.controller;

import kg.alatoo.food_delivery.dto.order.OrderRequestDto;
import kg.alatoo.food_delivery.dto.order.OrderResponseDto;
import kg.alatoo.food_delivery.enums.OrderStatus;
import kg.alatoo.food_delivery.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderService orderService;

  @Test
  public void testGetAllOrders() throws Exception {
    OrderResponseDto order = new OrderResponseDto(1L, 1L, 1L, List.of(1L, 2L), OrderStatus.PROCESSING, 2L);
    List<OrderResponseDto> orders = Collections.singletonList(order);
    Mockito.when(orderService.findAll()).thenReturn(orders);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].clientId").value(1L))
        .andExpect(jsonPath("$[0].restaurantId").value(1L))
        .andExpect(jsonPath("$[0].dishIds[0]").value(1L))
        .andExpect(jsonPath("$[0].dishIds[1]").value(2L))
        .andExpect(jsonPath("$[0].status").value("PROCESSING"))
        .andExpect(jsonPath("$[0].courierId").value(2L));
  }

  @Test
  public void testGetOrderById() throws Exception {
    OrderResponseDto order = new OrderResponseDto(1L, 1L, 1L, List.of(1L, 2L), OrderStatus.PROCESSING, 2L);
    Mockito.when(orderService.findById(1L)).thenReturn(order);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/1")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.clientId").value(1L))
        .andExpect(jsonPath("$.restaurantId").value(1L))
        .andExpect(jsonPath("$.dishIds[0]").value(1L))
        .andExpect(jsonPath("$.dishIds[1]").value(2L))
        .andExpect(jsonPath("$.status").value("PROCESSING"))
        .andExpect(jsonPath("$.courierId").value(2L));
  }

  @Test
  public void testCreateOrder() throws Exception {
    OrderRequestDto requestDto = new OrderRequestDto(1L, 1L, List.of(1L, 2L), OrderStatus.PROCESSING, 2L);
    OrderResponseDto responseDto = new OrderResponseDto(1L, 1L, 1L, List.of(1L, 2L), OrderStatus.PROCESSING, 2L);
    Mockito.when(orderService.createOrder(requestDto)).thenReturn(responseDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"clientId\":1,\"restaurantId\":1,\"dishIds\":[1,2],\"status\":\"PROCESSING\",\"courierId\":2}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.clientId").value(1L))
        .andExpect(jsonPath("$.restaurantId").value(1L))
        .andExpect(jsonPath("$.dishIds[0]").value(1L))
        .andExpect(jsonPath("$.dishIds[1]").value(2L))
        .andExpect(jsonPath("$.status").value("PROCESSING"))
        .andExpect(jsonPath("$.courierId").value(2L));
  }

  @Test
  public void testUpdateOrder() throws Exception {
    OrderRequestDto requestDto = new OrderRequestDto(1L, 1L, List.of(1L, 2L), OrderStatus.COMPLETED, 2L);
    OrderResponseDto responseDto = new OrderResponseDto(1L, 1L, 1L, List.of(1L, 2L), OrderStatus.COMPLETED, 2L);
    Mockito.when(orderService.updateOrder(1L, requestDto)).thenReturn(responseDto);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"clientId\":1,\"restaurantId\":1,\"dishIds\":[1,2],\"status\":\"COMPLETED\",\"courierId\":2}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.clientId").value(1L))
        .andExpect(jsonPath("$.restaurantId").value(1L))
        .andExpect(jsonPath("$.dishIds[0]").value(1L))
        .andExpect(jsonPath("$.dishIds[1]").value(2L))
        .andExpect(jsonPath("$.status").value("COMPLETED"))
        .andExpect(jsonPath("$.courierId").value(2L));
  }

  @Test
  public void testDeleteOrder() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/1"))
        .andExpect(status().isNoContent());

    Mockito.verify(orderService, Mockito.times(1)).deleteOrder(1L);
  }

  @Test
  public void testPatchOrder() throws Exception {
    OrderRequestDto requestDto = new OrderRequestDto(null, null, null, OrderStatus.COMPLETED, null);
    OrderResponseDto responseDto = new OrderResponseDto(1L, 1L, 1L, List.of(1L, 2L), OrderStatus.COMPLETED, 2L);
    Mockito.when(orderService.patchOrder(1L, requestDto)).thenReturn(responseDto);

    mockMvc.perform(MockMvcRequestBuilders.patch("/api/orders/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"status\":\"COMPLETED\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.clientId").value(1L))
        .andExpect(jsonPath("$.restaurantId").value(1L))
        .andExpect(jsonPath("$.dishIds[0]").value(1L))
        .andExpect(jsonPath("$.dishIds[1]").value(2L))
        .andExpect(jsonPath("$.status").value("COMPLETED"))
        .andExpect(jsonPath("$.courierId").value(2L));
  }

}

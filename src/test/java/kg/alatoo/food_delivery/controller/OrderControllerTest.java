package kg.alatoo.food_delivery.controller;

import kg.alatoo.food_delivery.config.SecurityConfiguration;
import kg.alatoo.food_delivery.service.security.JWTService;
import kg.alatoo.food_delivery.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import(SecurityConfiguration.class)
public class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private OrderService orderService;

  @MockBean
  private JWTService jwtService;

  @MockBean
  private AuthenticationProvider authenticationProvider;

  private String getAuthToken() {
    return "Bearer mock-token";
  }

  @Test
  @WithMockUser(roles = "CLIENT")
  public void testCreateOrder() throws Exception {
    String requestBody = "{\"clientId\":1,\"restaurantId\":1,\"dishIds\":[1,2]}";

    mockMvc.perform(MockMvcRequestBuilders.post("/api/orders")
            .with(csrf())
            .header("Authorization", getAuthToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isCreated());
  }

  @Test
  @WithMockUser(roles = "CLIENT")
  public void testGetAllOrders() throws Exception {
    when(orderService.findAll()).thenReturn(Collections.emptyList());

    mockMvc.perform(MockMvcRequestBuilders.get("/api/orders")
            .header("Authorization", getAuthToken()))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "RESTAURANT")
  public void testUpdateOrder_AsRestaurant() throws Exception {
    String requestBody = "{\"clientId\":1,\"restaurantId\":1,\"dishIds\":[1,2],\"status\":\"COMPLETED\",\"courierId\":2}";

    mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/1")
            .with(csrf())
            .header("Authorization", getAuthToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "CLIENT")
  public void testUpdateOrder_ForbiddenForClient() throws Exception {
    String requestBody = "{\"clientId\":1,\"restaurantId\":1,\"dishIds\":[1,2],\"status\":\"COMPLETED\",\"courierId\":2}";

    mockMvc.perform(MockMvcRequestBuilders.put("/api/orders/1")
            .with(csrf())
            .header("Authorization", getAuthToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isForbidden());
  }


  @Test
  @WithMockUser(roles = "ADMIN")
  public void testDeleteOrder_AsAdmin() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/1")
            .with(csrf())
            .header("Authorization", getAuthToken()))
        .andExpect(status().isNoContent());
  }

  @Test
  @WithMockUser(roles = "COURIER")
  public void testDeleteOrder_ForbiddenForCourier() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/orders/1")
            .with(csrf())
            .header("Authorization", getAuthToken()))
        .andExpect(status().isForbidden());
  }
}

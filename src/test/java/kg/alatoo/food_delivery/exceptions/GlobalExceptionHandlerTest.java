package kg.alatoo.food_delivery.exceptions;

import kg.alatoo.food_delivery.controller.DishController;
import kg.alatoo.food_delivery.exception.BadRequestException;
import kg.alatoo.food_delivery.exception.ResourceNotFoundException;
import kg.alatoo.food_delivery.service.DishService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DishController.class)
class GlobalExceptionHandlerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DishService dishService;

  @Test
  void handleResourceNotFoundException() throws Exception {
    Long dishId = 1L;
    when(dishService.findById(dishId)).thenThrow(new ResourceNotFoundException("Dish not found with id: " + dishId));

    mockMvc.perform(get("/api/dishes/" + dishId)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Dish not found with id: " + dishId))
        .andExpect(jsonPath("$.status").value(404))
        .andExpect(jsonPath("$.error").value("Not Found"));
  }

  @Test
  void handleBadRequestException() throws Exception {
    when(dishService.findById(-1L)).thenThrow(new BadRequestException("Invalid dish ID"));

    mockMvc.perform(get("/api/dishes/-1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Invalid dish ID"))
        .andExpect(jsonPath("$.status").value(400))
        .andExpect(jsonPath("$.error").value("Bad Request"));
  }

  @Test
  void handleGlobalException() throws Exception {
    when(dishService.findById(1L)).thenThrow(new RuntimeException("Unexpected error"));

    mockMvc.perform(get("/api/dishes/1")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("An error occurred: Unexpected error"))
        .andExpect(jsonPath("$.status").value(500))
        .andExpect(jsonPath("$.error").value("Internal Server Error"));
  }
}

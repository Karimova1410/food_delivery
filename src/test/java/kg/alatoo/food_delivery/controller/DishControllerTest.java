package kg.alatoo.food_delivery.controller;

import kg.alatoo.food_delivery.dto.dish.DishRequestDto;
import kg.alatoo.food_delivery.dto.dish.DishResponseDto;
import kg.alatoo.food_delivery.enums.DishCategory;
import kg.alatoo.food_delivery.service.DishService;
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

@WebMvcTest(DishController.class)
public class DishControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DishService dishService;

  @Test
  public void testGetAllDishes() throws Exception {
    DishResponseDto dish = new DishResponseDto(1L, "Burger", "Delicious beef burger", 5.99, DishCategory.MAIN_COURSE, 1L);
    List<DishResponseDto> dishes = Collections.singletonList(dish);
    Mockito.when(dishService.findAll()).thenReturn(dishes);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/dishes")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].name").value("Burger"))
        .andExpect(jsonPath("$[0].description").value("Delicious beef burger"))
        .andExpect(jsonPath("$[0].price").value(5.99))
        .andExpect(jsonPath("$[0].category").value("MAIN_COURSE"))
        .andExpect(jsonPath("$[0].restaurantId").value(1L));
  }

  @Test
  public void testGetDishById() throws Exception {
    DishResponseDto dish = new DishResponseDto(1L, "Burger", "Delicious beef burger", 5.99, DishCategory.MAIN_COURSE, 1L);
    Mockito.when(dishService.findById(1L)).thenReturn(dish);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/dishes/1")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Burger"))
        .andExpect(jsonPath("$.description").value("Delicious beef burger"))
        .andExpect(jsonPath("$.price").value(5.99))
        .andExpect(jsonPath("$.category").value("MAIN_COURSE"))
        .andExpect(jsonPath("$.restaurantId").value(1L));
  }

  @Test
  public void testCreateDish() throws Exception {
    DishRequestDto requestDto = new DishRequestDto("Sushi", "Assorted sushi rolls", 12.99, DishCategory.APPETIZER, 2L);
    DishResponseDto responseDto = new DishResponseDto(3L, "Sushi", "Assorted sushi rolls", 12.99, DishCategory.APPETIZER, 2L);
    Mockito.when(dishService.createDish(requestDto)).thenReturn(responseDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Sushi\",\"description\":\"Assorted sushi rolls\",\"price\":12.99,\"category\":\"APPETIZER\",\"restaurantId\":2}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(3L))
        .andExpect(jsonPath("$.name").value("Sushi"))
        .andExpect(jsonPath("$.description").value("Assorted sushi rolls"))
        .andExpect(jsonPath("$.price").value(12.99))
        .andExpect(jsonPath("$.category").value("APPETIZER"))
        .andExpect(jsonPath("$.restaurantId").value(2L));
  }

  @Test
  public void testUpdateDish() throws Exception {
    DishRequestDto requestDto = new DishRequestDto("Updated Sushi", "Updated sushi rolls", 15.99, DishCategory.APPETIZER, 2L);
    DishResponseDto responseDto = new DishResponseDto(3L, "Updated Sushi", "Updated sushi rolls", 15.99, DishCategory.APPETIZER, 2L);
    Mockito.when(dishService.updateDish(3L, requestDto)).thenReturn(responseDto);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/dishes/3")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Updated Sushi\",\"description\":\"Updated sushi rolls\",\"price\":15.99,\"category\":\"APPETIZER\",\"restaurantId\":2}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(3L))
        .andExpect(jsonPath("$.name").value("Updated Sushi"))
        .andExpect(jsonPath("$.description").value("Updated sushi rolls"))
        .andExpect(jsonPath("$.price").value(15.99))
        .andExpect(jsonPath("$.category").value("APPETIZER"))
        .andExpect(jsonPath("$.restaurantId").value(2L));
  }

  @Test
  public void testDeleteDish() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/dishes/1"))
        .andExpect(status().isNoContent());

    Mockito.verify(dishService, Mockito.times(1)).deleteDish(1L);
  }
}
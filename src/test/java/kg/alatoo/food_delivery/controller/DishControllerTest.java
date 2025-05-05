package kg.alatoo.food_delivery.controller;

import kg.alatoo.food_delivery.config.SecurityConfiguration;
import kg.alatoo.food_delivery.dto.dish.DishRequestDto;
import kg.alatoo.food_delivery.dto.dish.DishResponseDto;
import kg.alatoo.food_delivery.enums.DishCategory;
import kg.alatoo.food_delivery.service.DishService;
import kg.alatoo.food_delivery.service.security.JWTService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DishController.class)
@Import(SecurityConfiguration.class)
public class DishControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private DishService dishService;

  @MockBean
  @Autowired
  private JWTService jwtService;

  @MockBean
  private AuthenticationProvider authenticationProvider;

  private String getAuthToken() {
    return "Bearer mock-token";
  }

  @Test
  @WithMockUser(roles = "CLIENT")
  public void testGetAllDishes() throws Exception {
    DishResponseDto dish = new DishResponseDto(1L, "Burger", "Delicious beef burger", 5.99, DishCategory.MAIN_COURSE, 1L);
    List<DishResponseDto> dishes = Collections.singletonList(dish);
    Mockito.when(dishService.findAll()).thenReturn(dishes);

    mockMvc.perform(get("/api/dishes")
            .header("Authorization", getAuthToken())
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
  @WithMockUser(roles = "CLIENT")
  public void testGetDishById() throws Exception {
    DishResponseDto dish = new DishResponseDto(1L, "Burger", "Delicious beef burger", 5.99, DishCategory.MAIN_COURSE, 1L);
    Mockito.when(dishService.findById(1L)).thenReturn(dish);

    mockMvc.perform(get("/api/dishes/1")
            .header("Authorization", getAuthToken())
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
  @WithMockUser(roles = "RESTAURANT")
  public void testCreateDish() throws Exception {
    DishRequestDto requestDto = new DishRequestDto("Sushi", "Assorted sushi rolls", 12.99, DishCategory.APPETIZER, 2L);
    DishResponseDto responseDto = new DishResponseDto(3L, "Sushi", "Assorted sushi rolls", 12.99, DishCategory.APPETIZER, 2L);
    Mockito.when(dishService.createDish(requestDto)).thenReturn(responseDto);

    mockMvc.perform(post("/api/dishes")
            .with(csrf())
            .header("Authorization", getAuthToken())
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
  @WithMockUser(roles = "RESTAURANT")
  public void testUpdateDish() throws Exception {
    DishRequestDto requestDto = new DishRequestDto("Updated Sushi", "Updated sushi rolls", 15.99, DishCategory.APPETIZER, 2L);
    DishResponseDto responseDto = new DishResponseDto(3L, "Updated Sushi", "Updated sushi rolls", 15.99, DishCategory.APPETIZER, 2L);
    Mockito.when(dishService.updateDish(3L, requestDto)).thenReturn(responseDto);

    mockMvc.perform(put("/api/dishes/3")
            .with(csrf())
            .header("Authorization", getAuthToken())
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
  @WithMockUser(roles = "RESTAURANT")
  public void testDeleteDish() throws Exception {
    mockMvc.perform(delete("/api/dishes/1")
            .with(csrf())
            .header("Authorization", getAuthToken()))
        .andExpect(status().isNoContent());

    Mockito.verify(dishService, Mockito.times(1)).deleteDish(1L);
  }

  @Test
  @WithMockUser(roles = "RESTAURANT")
  public void testPatchDish() throws Exception {
    DishRequestDto requestDto = new DishRequestDto("Updated Sushi", null, 15.99, null, null);
    DishResponseDto responseDto = new DishResponseDto(4L, "Updated Sushi", "Assorted sushi rolls", 15.99, DishCategory.MAIN_COURSE, 2L);
    Mockito.when(dishService.patchDish(4L, requestDto)).thenReturn(responseDto);

    mockMvc.perform(patch("/api/dishes/4")
            .with(csrf())
            .header("Authorization", getAuthToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Updated Sushi\",\"price\":15.99}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(4L))
        .andExpect(jsonPath("$.name").value("Updated Sushi"))
        .andExpect(jsonPath("$.description").value("Assorted sushi rolls"))
        .andExpect(jsonPath("$.price").value(15.99))
        .andExpect(jsonPath("$.category").value("MAIN_COURSE"))
        .andExpect(jsonPath("$.restaurantId").value(2L));
  }

  @Test
  @WithMockUser(roles = "CLIENT")
  public void testCreateDish_ForbiddenForClient() throws Exception {
    mockMvc.perform(post("/api/dishes")
            .with(csrf())
            .header("Authorization", getAuthToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Sushi\",\"description\":\"Assorted sushi rolls\",\"price\":12.99,\"category\":\"APPETIZER\",\"restaurantId\":2}"))
        .andExpect(status().isForbidden());
  }
}
package kg.alatoo.food_delivery.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantRequestDto;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantResponseDto;
import kg.alatoo.food_delivery.service.RestaurantService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private RestaurantService restaurantService;

  @Test
  public void testCreateRestaurant() throws Exception {
    RestaurantRequestDto requestDto = new RestaurantRequestDto("Sushi Place", "Chui 132", List.of(1L, 2L));
    RestaurantResponseDto responseDto = new RestaurantResponseDto(1L, "Sushi Place", "Chui 132", List.of(1L, 2L));

    Mockito.when(restaurantService.createRestaurant(requestDto)).thenReturn(responseDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/restaurants")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Sushi Place\",\"address\":\"Chui 132\",\"dishIds\":[1,2]}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Sushi Place"))
        .andExpect(jsonPath("$.address").value("Chui 132"))
        .andExpect(jsonPath("$.dishIds[0]").value(1L))
        .andExpect(jsonPath("$.dishIds[1]").value(2L));
  }

  @Test
  public void testGetRestaurantById() throws Exception {
    RestaurantResponseDto responseDto = new RestaurantResponseDto(1L, "Sushi Place", "Chui 132", List.of(1L, 2L));
    Mockito.when(restaurantService.getRestaurantById(1L)).thenReturn(responseDto);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/restaurants/1")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Sushi Place"))
        .andExpect(jsonPath("$.address").value("Chui 132"))
        .andExpect(jsonPath("$.dishIds[0]").value(1L))
        .andExpect(jsonPath("$.dishIds[1]").value(2L));
  }

  @Test
  public void testPatchRestaurant() throws Exception {
    RestaurantRequestDto requestDto = new RestaurantRequestDto("Updated Sushi Place", null, null);
    RestaurantResponseDto responseDto = new RestaurantResponseDto(1L, "Updated Sushi Place", "Chui 132", List.of(1L, 2L));

    Mockito.when(restaurantService.patchRestaurant(1L, requestDto)).thenReturn(responseDto);

    mockMvc.perform(MockMvcRequestBuilders.patch("/api/restaurants/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Updated Sushi Place\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Updated Sushi Place"))
        .andExpect(jsonPath("$.address").value("Chui 132"))
        .andExpect(jsonPath("$.dishIds[0]").value(1L))
        .andExpect(jsonPath("$.dishIds[1]").value(2L));
  }

  @Test
  public void testGetAllRestaurants() throws Exception {
    RestaurantResponseDto restaurant1 = new RestaurantResponseDto(1L, "Sushi Place", "Chui 132", List.of(1L, 2L));
    RestaurantResponseDto restaurant2 = new RestaurantResponseDto(2L, "Pizza Place", "456 Elm St", List.of(3L, 4L));
    List<RestaurantResponseDto> restaurants = List.of(restaurant1, restaurant2);

    Mockito.when(restaurantService.getAllRestaurants()).thenReturn(restaurants);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/restaurants")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].name").value("Sushi Place"))
        .andExpect(jsonPath("$[0].address").value("Chui 132"))
        .andExpect(jsonPath("$[0].dishIds[0]").value(1L))
        .andExpect(jsonPath("$[0].dishIds[1]").value(2L))
        .andExpect(jsonPath("$[1].id").value(2L))
        .andExpect(jsonPath("$[1].name").value("Pizza Place"))
        .andExpect(jsonPath("$[1].address").value("456 Elm St"))
        .andExpect(jsonPath("$[1].dishIds[0]").value(3L))
        .andExpect(jsonPath("$[1].dishIds[1]").value(4L));
  }

  @Test
  public void testUpdateRestaurant() throws Exception {
    RestaurantRequestDto requestDto = new RestaurantRequestDto("Updated Sushi Place", "456 New St", List.of(5L, 6L));
    RestaurantResponseDto responseDto = new RestaurantResponseDto(1L, "Updated Sushi Place", "456 New St", List.of(5L, 6L));

    Mockito.when(restaurantService.updateRestaurant(1L, requestDto)).thenReturn(responseDto);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/restaurants/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"name\":\"Updated Sushi Place\",\"address\":\"456 New St\",\"dishIds\":[5,6]}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.name").value("Updated Sushi Place"))
        .andExpect(jsonPath("$.address").value("456 New St"))
        .andExpect(jsonPath("$.dishIds[0]").value(5L))
        .andExpect(jsonPath("$.dishIds[1]").value(6L));
  }

  @Test
  public void testDeleteRestaurant() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/restaurants/1"))
        .andExpect(status().isNoContent());

    Mockito.verify(restaurantService, Mockito.times(1)).deleteRestaurant(1L);
  }
}
package kg.alatoo.food_delivery.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import kg.alatoo.food_delivery.dto.user.UserRequestDto;
import kg.alatoo.food_delivery.dto.user.UserResponseDto;
import kg.alatoo.food_delivery.enums.UserRole;
import kg.alatoo.food_delivery.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  public void testGetAllUsers() throws Exception {
    Mockito.when(userService.findAll()).thenReturn(Collections.emptyList());

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json("[]"));
  }

  @Test
  public void testGetUserById() throws Exception {
    UserResponseDto user = new UserResponseDto(1L, "admin", UserRole.CLIENT);
    Mockito.when(userService.findById(1L)).thenReturn(user);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("admin"));
  }

  @Test
  public void testCreateUser() throws Exception {
    UserRequestDto requestDto = new UserRequestDto("admin", "admin123", UserRole.ADMIN);
    UserResponseDto responseDto = new UserResponseDto(1L, "admin", UserRole.ADMIN);

    Mockito.when(userService.createUser(requestDto)).thenReturn(responseDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"admin\",\"password\":\"admin123\",\"role\":\"ADMIN\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("admin"));
  }

  @Test
  public void testDeleteUser() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1"))
        .andExpect(status().isNoContent());

    Mockito.verify(userService, Mockito.times(1)).deleteUser(1L);
  }

}

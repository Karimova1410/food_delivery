package kg.alatoo.food_delivery.controller;

import kg.alatoo.food_delivery.dto.user.UserRequestDto;
import kg.alatoo.food_delivery.dto.user.UserResponseDto;
import kg.alatoo.food_delivery.enums.UserRole;
import kg.alatoo.food_delivery.service.UserService;
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

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  public void testGetAllUsers() throws Exception {
    UserResponseDto user = new UserResponseDto(1L, "admin", UserRole.ADMIN);
    List<UserResponseDto> users = Collections.singletonList(user);
    Mockito.when(userService.findAll()).thenReturn(users);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].username").value("admin"))
        .andExpect(jsonPath("$[0].role").value("ADMIN"));
  }

  @Test
  public void testGetUserById() throws Exception {
    UserResponseDto user = new UserResponseDto(1L, "admin", UserRole.ADMIN);
    Mockito.when(userService.findById(1L)).thenReturn(user);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/1")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("admin"))
        .andExpect(jsonPath("$.role").value("ADMIN"));
  }

  @Test
  public void testCreateUser() throws Exception {
    UserRequestDto requestDto = new UserRequestDto("newuser", "P@assword123", UserRole.CLIENT);
    UserResponseDto responseDto = new UserResponseDto(1L, "newuser", UserRole.CLIENT);
    Mockito.when(userService.createUser(requestDto)).thenReturn(responseDto);

    mockMvc.perform(MockMvcRequestBuilders.post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"newuser\",\"password\":\"P@assword123\",\"role\":\"CLIENT\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("newuser"))
        .andExpect(jsonPath("$.role").value("CLIENT"));
  }

  @Test
  public void testUpdateUser() throws Exception {
    UserRequestDto requestDto = new UserRequestDto("updateduser", "P@assword123", UserRole.ADMIN);
    UserResponseDto responseDto = new UserResponseDto(1L, "updateduser", UserRole.ADMIN);
    Mockito.when(userService.updateUser(1L, requestDto)).thenReturn(responseDto);

    mockMvc.perform(MockMvcRequestBuilders.put("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"updateduser\",\"password\":\"P@assword123\",\"role\":\"ADMIN\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("updateduser"))
        .andExpect(jsonPath("$.role").value("ADMIN"));
  }

  @Test
  public void testPatchUser() throws Exception {
    UserRequestDto requestDto = new UserRequestDto("patcheduser", null, null);
    UserResponseDto responseDto = new UserResponseDto(1L, "patcheduser", UserRole.ADMIN);
    Mockito.when(userService.patchUser(1L, requestDto)).thenReturn(responseDto);

    mockMvc.perform(MockMvcRequestBuilders.patch("/api/users/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"patcheduser\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("patcheduser"))
        .andExpect(jsonPath("$.role").value("ADMIN"));
  }

  @Test
  public void testDeleteUser() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/1"))
        .andExpect(status().isNoContent());

    Mockito.verify(userService, Mockito.times(1)).deleteUser(1L);
  }
}
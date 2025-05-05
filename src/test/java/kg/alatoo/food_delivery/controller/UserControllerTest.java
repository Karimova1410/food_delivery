package kg.alatoo.food_delivery.controller;

import kg.alatoo.food_delivery.config.SecurityConfiguration;
import kg.alatoo.food_delivery.dto.user.UserRequestDto;
import kg.alatoo.food_delivery.dto.user.UserResponseDto;
import kg.alatoo.food_delivery.enums.Role;
import kg.alatoo.food_delivery.service.security.JWTService;
import kg.alatoo.food_delivery.service.UserService;
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

@WebMvcTest(UserController.class)
@Import(SecurityConfiguration.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private JWTService jwtService;

  @MockBean
  private AuthenticationProvider authenticationProvider;

  private String getAuthToken() {
    return "Bearer mock-token";
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testGetAllUsers() throws Exception {
    UserResponseDto user = new UserResponseDto(1L, "roza", Role.ADMIN);
    List<UserResponseDto> users = Collections.singletonList(user);
    Mockito.when(userService.findAll()).thenReturn(users);

    mockMvc.perform(get("/api/users")
            .header("Authorization", getAuthToken())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1L))
        .andExpect(jsonPath("$[0].username").value("roza"))
        .andExpect(jsonPath("$[0].role").value("ADMIN"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testGetUserById() throws Exception {
    UserResponseDto user = new UserResponseDto(1L, "roza", Role.ADMIN);
    Mockito.when(userService.findById(1L)).thenReturn(user);

    mockMvc.perform(get("/api/users/1")
            .header("Authorization", getAuthToken())
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("roza"))
        .andExpect(jsonPath("$.role").value("ADMIN"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testCreateUser() throws Exception {
    UserRequestDto requestDto = new UserRequestDto("roza", "P@assword123", Role.CLIENT);
    UserResponseDto responseDto = new UserResponseDto(1L, "roza", Role.CLIENT);
    Mockito.when(userService.createUser(requestDto)).thenReturn(responseDto);

    mockMvc.perform(post("/api/users")
            .with(csrf())
            .header("Authorization", getAuthToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"roza\",\"password\":\"P@assword123\",\"role\":\"CLIENT\"}"))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("roza"))
        .andExpect(jsonPath("$.role").value("CLIENT"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testUpdateUser() throws Exception {
    UserRequestDto requestDto = new UserRequestDto("roza", "P@assword123", Role.ADMIN);
    UserResponseDto responseDto = new UserResponseDto(1L, "roza", Role.ADMIN);
    Mockito.when(userService.updateUser(1L, requestDto)).thenReturn(responseDto);

    mockMvc.perform(put("/api/users/1")
            .with(csrf())
            .header("Authorization", getAuthToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"roza\",\"password\":\"P@assword123\",\"role\":\"ADMIN\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("roza"))
        .andExpect(jsonPath("$.role").value("ADMIN"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testPatchUser() throws Exception {
    UserRequestDto requestDto = new UserRequestDto("roza", null, null);
    UserResponseDto responseDto = new UserResponseDto(1L, "roza", Role.ADMIN);
    Mockito.when(userService.patchUser(1L, requestDto)).thenReturn(responseDto);

    mockMvc.perform(patch("/api/users/1")
            .with(csrf())
            .header("Authorization", getAuthToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"roza\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.username").value("roza"))
        .andExpect(jsonPath("$.role").value("ADMIN"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testDeleteUser() throws Exception {
    mockMvc.perform(delete("/api/users/1")
            .with(csrf())
            .header("Authorization", getAuthToken()))
        .andExpect(status().isNoContent());

    Mockito.verify(userService, Mockito.times(1)).deleteUser(1L);
  }

  @Test
  @WithMockUser(roles = "CLIENT")
  public void testGetAllUsers_ForbiddenForClient() throws Exception {
    mockMvc.perform(get("/api/users")
            .header("Authorization", getAuthToken()))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "RESTAURANT")
  public void testCreateUser_ForbiddenForRestaurant() throws Exception {
    mockMvc.perform(post("/api/users")
            .with(csrf())
            .header("Authorization", getAuthToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\":\"roza\",\"password\":\"P@assword123\",\"role\":\"CLIENT\"}"))
        .andExpect(status().isForbidden());
  }
}
package kg.alatoo.food_delivery.service;

import jakarta.validation.Valid;
import kg.alatoo.food_delivery.dto.user.UserRequestDto;
import kg.alatoo.food_delivery.dto.user.UserResponseDto;
import kg.alatoo.food_delivery.entity.User;

public interface UserService {

  UserResponseDto registerUser(@Valid UserRequestDto user);
  UserResponseDto findByUsername(String username);
}

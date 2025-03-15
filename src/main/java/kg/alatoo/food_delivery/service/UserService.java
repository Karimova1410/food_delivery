package kg.alatoo.food_delivery.service;

import java.util.List;
import kg.alatoo.food_delivery.dto.user.UserRequestDto;
import kg.alatoo.food_delivery.dto.user.UserResponseDto;

public interface UserService {

  List<UserResponseDto> findAll();

  UserResponseDto findById(Long id);

  UserResponseDto createUser(UserRequestDto userRequestDto);

  UserResponseDto updateUser(Long id, UserRequestDto userRequestDto);

  UserResponseDto patchUser(Long id, UserRequestDto userRequestDto);

  void deleteUser(Long id);
}

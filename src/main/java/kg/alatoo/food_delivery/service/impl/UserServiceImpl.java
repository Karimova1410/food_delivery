package kg.alatoo.food_delivery.service.impl;

import java.util.List;
import kg.alatoo.food_delivery.dto.user.UserRequestDto;
import kg.alatoo.food_delivery.dto.user.UserResponseDto;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.mapper.mapstruct.UserMapstructMapper;
import kg.alatoo.food_delivery.repository.UserRepository;
import kg.alatoo.food_delivery.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapstructMapper userMapper;

  public UserServiceImpl(UserRepository userRepository, UserMapstructMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  @Override
  public UserResponseDto registerUser(UserRequestDto userRequest) {
    User user = userMapper.toEntity(userRequest);
    User userSaved = userRepository.save(user);
    return userMapper.toDto(userSaved);
  }

  @Override
  public UserResponseDto findByUsername(String username) {
    User user = userRepository.findByUsername(username);
    return new UserResponseDto(
        user.getId(),
        user.getUsername(),
        user.getRole()
    );
  }

  @Override
  public List<UserResponseDto> findAll() {
    return List.of();
  }

  @Override
  public UserResponseDto findById(Long id) {
    return null;
  }

  @Override
  public UserResponseDto createUser(UserRequestDto userRequestDto) {
    return null;
  }

  @Override
  public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
    return null;
  }

  @Override
  public UserResponseDto patchUser(Long id, UserRequestDto userRequestDto) {
    return null;
  }

  @Override
  public void deleteUser(Long id) {

  }
}

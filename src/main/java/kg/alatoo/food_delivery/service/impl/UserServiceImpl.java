package kg.alatoo.food_delivery.service.impl;

import java.util.List;
import java.util.stream.Collectors;
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
  public List<UserResponseDto> findAll() {
    List<User> users = userRepository.findAll();
    return users.stream()
        .map(userMapper::toDto)
        .collect(Collectors.toList());
  }

  @Override
  public UserResponseDto findById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    return userMapper.toDto(user);
  }

  @Override
  public UserResponseDto createUser(UserRequestDto userRequestDto) {
    User user = userMapper.toEntity(userRequestDto);
    User userSaved = userRepository.save(user);
    return userMapper.toDto(userSaved);
  }

  @Override
  public UserResponseDto updateUser(Long id, UserRequestDto userRequestDto) {
    User existingUser = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

    existingUser.setUsername(userRequestDto.username());
    existingUser.setPassword(userRequestDto.password());
    existingUser.setRole(userRequestDto.role());

    User updatedUser = userRepository.save(existingUser);
    return userMapper.toDto(updatedUser);
  }

  @Override
  public UserResponseDto patchUser(Long id, UserRequestDto userRequestDto) {
    User existingUser = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

    if (userRequestDto.username() != null) {
      existingUser.setUsername(userRequestDto.username());
    }
    if (userRequestDto.password() != null) {
      existingUser.setPassword(userRequestDto.password());
    }
    if (userRequestDto.role() != null) {
      existingUser.setRole(userRequestDto.role());
    }

    User patchedUser = userRepository.save(existingUser);
    return userMapper.toDto(patchedUser);
  }

  @Override
  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new RuntimeException("User not found with id: " + id);
    }
    userRepository.deleteById(id);
  }
}

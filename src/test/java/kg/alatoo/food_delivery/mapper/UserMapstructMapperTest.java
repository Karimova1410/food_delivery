package kg.alatoo.food_delivery.mapper;

import kg.alatoo.food_delivery.dto.user.UserRequestDto;
import kg.alatoo.food_delivery.dto.user.UserResponseDto;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.enums.Role;
import kg.alatoo.food_delivery.mapper.mapstruct.UserMapstructMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapstructMapperTest {

  private final UserMapstructMapper userMapstructMapper = Mappers.getMapper(UserMapstructMapper.class);

  @Test
  void testToEntity() {
    UserRequestDto userRequestDto = new UserRequestDto("roza", "Password123!", Role.CLIENT);

    User user = userMapstructMapper.toEntity(userRequestDto);

    assertThat(user).isNotNull();
    assertThat(user.getUsername()).isEqualTo(userRequestDto.username());
    assertThat(user.getPassword()).isEqualTo(userRequestDto.password());
    assertThat(user.getRole()).isEqualTo(userRequestDto.role());
  }

  @Test
  void testToDto() {
    User user = User.builder()
        .id(1L)
        .username("roza")
        .password("Password123!")
        .role(Role.CLIENT)
        .build();

    UserResponseDto userResponseDto = userMapstructMapper.toDto(user);

    assertThat(userResponseDto).isNotNull();
    assertThat(userResponseDto.id()).isEqualTo(user.getId());
    assertThat(userResponseDto.username()).isEqualTo(user.getUsername());
    assertThat(userResponseDto.role()).isEqualTo(user.getRole());
  }
}
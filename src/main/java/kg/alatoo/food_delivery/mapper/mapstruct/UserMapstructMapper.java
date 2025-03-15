package kg.alatoo.food_delivery.mapper.mapstruct;

import kg.alatoo.food_delivery.dto.user.UserRequestDto;
import kg.alatoo.food_delivery.dto.user.UserResponseDto;
import kg.alatoo.food_delivery.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapstructMapper {

  User toEntity(UserRequestDto userRequestDto);

  UserResponseDto toDto(User user);
}

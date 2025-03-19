package kg.alatoo.food_delivery.controller;

import jakarta.validation.Valid;
import java.util.List;
import kg.alatoo.food_delivery.controller.swagger.UserControllerSwagger;
import kg.alatoo.food_delivery.dto.user.UserRequestDto;
import kg.alatoo.food_delivery.dto.user.UserResponseDto;
import kg.alatoo.food_delivery.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController implements UserControllerSwagger {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<List<UserResponseDto>> getAllUsers() {
    List<UserResponseDto> users = userService.findAll();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
    UserResponseDto user = userService.findById(id);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid UserRequestDto userRequestDto) {
    UserResponseDto createdUser = userService.createUser(userRequestDto);
    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDto> updateUser(
      @PathVariable Long id,
      @RequestBody @Valid UserRequestDto userRequestDto) {
    UserResponseDto updatedUser = userService.updateUser(id, userRequestDto);
    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<UserResponseDto> patchUser(
      @PathVariable Long id,
      @RequestBody @Valid UserRequestDto userRequestDto) {
    UserResponseDto patchedUser = userService.patchUser(id, userRequestDto);
    return new ResponseEntity<>(patchedUser, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }
}

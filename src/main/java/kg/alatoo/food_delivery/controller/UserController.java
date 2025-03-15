package kg.alatoo.food_delivery.controller;

import jakarta.validation.Valid;
import java.util.List;
import kg.alatoo.food_delivery.dto.user.UserRequestDto;
import kg.alatoo.food_delivery.dto.user.UserResponseDto;
import kg.alatoo.food_delivery.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping
  public List<UserResponseDto> getAllUsers() {
    return userService.findAll();
  }

  @GetMapping("/{id}")
  public UserResponseDto getUserById(@PathVariable Long id) {
    return userService.findById(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
    return userService.createUser(userRequestDto);
  }

  @PutMapping("/{id}")
  public UserResponseDto updateUser(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
    return userService.updateUser(id, userRequestDto);
  }

  @PatchMapping("/{id}")
  public UserResponseDto patchUser(@PathVariable Long id, @RequestBody UserRequestDto userRequestDto) {
    return userService.patchUser(id, userRequestDto);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
  }

  @PostMapping("/register")
  public UserResponseDto registerUser(@RequestBody @Valid UserRequestDto user) {
    return userService.registerUser(user);
  }

}

package kg.alatoo.food_delivery.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.alatoo.food_delivery.dto.user.UserRequestDto;
import kg.alatoo.food_delivery.dto.user.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "User Controller", description = "APIs for managing users")
public interface UserControllerSwagger {

  @Operation(summary = "Create a new user", description = "Create a new user with the provided details")
  @ApiResponse(responseCode = "201", description = "User created successfully",
      content = @Content(schema = @Schema(implementation = UserResponseDto.class)))
  ResponseEntity<UserResponseDto> createUser(
      @Parameter(description = "User details to create", required = true)
      @Valid @RequestBody UserRequestDto userRequestDto);

  @Operation(summary = "Get user by ID", description = "Retrieve a user by their ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved user",
          content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  ResponseEntity<UserResponseDto> getUserById(
      @Parameter(description = "ID of the user to be retrieved", required = true)
      Long id);

  @Operation(summary = "Get all users", description = "Retrieve a list of all users")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users",
      content = @Content(schema = @Schema(implementation = UserResponseDto.class)))
  ResponseEntity<List<UserResponseDto>> getAllUsers();

  @Operation(summary = "Update a user", description = "Update an existing user by their ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User updated successfully",
          content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  ResponseEntity<UserResponseDto> updateUser(
      @Parameter(description = "ID of the user to be updated", required = true)
      Long id,
      @Parameter(description = "Updated user details", required = true)
      @Valid @RequestBody UserRequestDto userRequestDto);

  @Operation(summary = "Partially update a user", description = "Partially update an existing user by their ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "User patched successfully",
          content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  ResponseEntity<UserResponseDto> patchUser(
      @Parameter(description = "ID of the user to be patched", required = true)
      Long id,
      @Parameter(description = "Partial user details to update", required = true)
      @Valid @RequestBody UserRequestDto userRequestDto);

  @Operation(summary = "Delete a user", description = "Delete a user by their ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "User deleted successfully"),
      @ApiResponse(responseCode = "404", description = "User not found")
  })
  ResponseEntity<Void> deleteUser(
      @Parameter(description = "ID of the user to be deleted", required = true)
      Long id);
}

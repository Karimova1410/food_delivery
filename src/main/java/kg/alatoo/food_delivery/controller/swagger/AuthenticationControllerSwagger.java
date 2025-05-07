package kg.alatoo.food_delivery.controller.swagger;

import kg.alatoo.food_delivery.dto.auth.LoginResponse;
import kg.alatoo.food_delivery.dto.auth.LoginUserDto;
import kg.alatoo.food_delivery.dto.auth.RegisterUserDto;
import kg.alatoo.food_delivery.dto.auth.TokenRefreshRequest;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.dao.UserDetailsDao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Tag(name = "Authentication Controller", description = "APIs for user authentication and token management")
public interface AuthenticationControllerSwagger {

  @Operation(summary = "Register new user", description = "Create a new user account")
  @ApiResponse(responseCode = "200", description = "User registered successfully",
      content = @Content(schema = @Schema(implementation = User.class)))
  ResponseEntity<User> register(RegisterUserDto registerUserDto);

  @Operation(summary = "Login user", description = "Authenticate user and return tokens")
  @ApiResponse(responseCode = "200", description = "Login successful",
      content = @Content(schema = @Schema(implementation = LoginResponse.class)))
  ResponseEntity<LoginResponse> authenticate(LoginUserDto loginUserDto);

  @Operation(summary = "Refresh JWT token", description = "Get a new access token using refresh token")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Token refreshed successfully",
          content = @Content(schema = @Schema(implementation = LoginResponse.class))),
      @ApiResponse(responseCode = "403", description = "Refresh token invalid or expired")
  })
  ResponseEntity<LoginResponse> refreshToken(TokenRefreshRequest request);

  @Operation(summary = "Logout user", description = "Invalidate refresh token and logout the user")
  @ApiResponse(responseCode = "200", description = "User logged out successfully")
  ResponseEntity<?> logoutUser(@AuthenticationPrincipal UserDetailsDao userDetails);
}

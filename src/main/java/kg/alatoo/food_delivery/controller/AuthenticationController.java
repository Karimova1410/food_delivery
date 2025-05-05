package kg.alatoo.food_delivery.controller;

import kg.alatoo.food_delivery.dao.UserDetailsDao;
import kg.alatoo.food_delivery.dto.auth.LoginResponse;
import kg.alatoo.food_delivery.dto.auth.LoginUserDto;
import kg.alatoo.food_delivery.dto.auth.RegisterUserDto;
import kg.alatoo.food_delivery.dto.auth.TokenRefreshRequest;
import kg.alatoo.food_delivery.entity.RefreshToken;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.exception.TokenRefreshException;
import kg.alatoo.food_delivery.mapper.UserMapper;
import kg.alatoo.food_delivery.repository.UserRepository;
import kg.alatoo.food_delivery.service.security.AuthenticationService;
import kg.alatoo.food_delivery.service.security.JWTService;
import kg.alatoo.food_delivery.service.security.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {

  private final JWTService jwtService;
  private final AuthenticationService authenticationService;
  private final RefreshTokenService refreshTokenService;

  private final UserMapper userMapper;
  private final UserRepository userRepository;


  public AuthenticationController(JWTService jwtService,
      AuthenticationService authenticationService,
      RefreshTokenService refreshTokenService, UserMapper userMapper,
      UserRepository userRepository) {
    this.jwtService = jwtService;
    this.authenticationService = authenticationService;
    this.refreshTokenService = refreshTokenService;
    this.userMapper = userMapper;
    this.userRepository = userRepository;
  }

  @PostMapping("/signup")
  public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
    User registeredUser = authenticationService.signup(registerUserDto);
    return ResponseEntity.ok(registeredUser);
  }


  @PostMapping("/login")
  public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
    User authenticatedUser = authenticationService.authenticate(loginUserDto);

    String jwtToken = jwtService.generateToken(userMapper.toUserDetailsDao(authenticatedUser));
    RefreshToken refreshToken = refreshTokenService.createRefreshToken(authenticatedUser);

    LoginResponse loginResponse = new LoginResponse();
    loginResponse.setAccessToken(jwtToken);
    loginResponse.setExpiresIn(jwtService.getExpirationTime());
    loginResponse.setRefreshToken(refreshToken.getToken().toString());

    return ResponseEntity.ok(loginResponse);
  }

  @PostMapping("/refresh")
  public ResponseEntity<LoginResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
    String requestRefreshToken = request.getRefreshToken();

    return refreshTokenService.findByToken(requestRefreshToken)
        .map(refreshTokenService::verifyExpiration)
        .map(RefreshToken::getUser)
        .map(user -> {
          String token = jwtService.generateTokenFromUsername(user.getEmail());
          LoginResponse response = new LoginResponse();
          response.setAccessToken(token);
          response.setExpiresIn(jwtService.getExpirationTime());
          response.setRefreshToken(requestRefreshToken);
          return ResponseEntity.ok(response);
        })
        .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
            "Refresh token is not in database!"));
  }

  @PostMapping("/logout")
  public ResponseEntity<?> logoutUser(@AuthenticationPrincipal UserDetailsDao userDetails) {
    User user = userRepository.findByEmail(userDetails.getUsername())
        .orElseThrow(() -> new RuntimeException("User not found"));

    authenticationService.logout(user);
    return ResponseEntity.ok("Logged out successfully");
  }

}
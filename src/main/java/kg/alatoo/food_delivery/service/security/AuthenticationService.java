package kg.alatoo.food_delivery.service.security;

import kg.alatoo.food_delivery.dto.auth.LoginUserDto;
import kg.alatoo.food_delivery.dto.auth.RegisterUserDto;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  private final RefreshTokenService refreshTokenService;

  public AuthenticationService(
      UserRepository userRepository,
      AuthenticationManager authenticationManager,
      PasswordEncoder passwordEncoder, RefreshTokenService refreshTokenService
  ) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.refreshTokenService = refreshTokenService;
  }

  public User signup(RegisterUserDto input) {
    User user = new User();
    user.setName(input.getName());
    user.setSurname(input.getSurname());
    user.setEmail(input.getEmail());
    user.setRole(input.getRole());
    user.setUsername(input.getUsername());
    user.setPassword(passwordEncoder.encode(input.getPassword()));
    return userRepository.save(user);
  }

  public User authenticate(LoginUserDto input) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            input.getEmail(),
            input.getPassword()
        )
    );

    return userRepository.findByEmail(input.getEmail())
        .orElseThrow();
  }

  public void logout(User user) {
    refreshTokenService.deleteByUserId(user.getId());
  }

}

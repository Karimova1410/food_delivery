package kg.alatoo.food_delivery.oauth;

import kg.alatoo.food_delivery.dto.auth.LoginUserDto;
import kg.alatoo.food_delivery.dto.auth.RegisterUserDto;
import kg.alatoo.food_delivery.entity.RefreshToken;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.mapper.UserMapper;
import kg.alatoo.food_delivery.service.UserService;
import kg.alatoo.food_delivery.service.security.AuthenticationService;
import kg.alatoo.food_delivery.service.security.JWTService;
import kg.alatoo.food_delivery.service.security.RefreshTokenService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JWTService jwtService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;

    public AuthController(UserService userService, JWTService jwtService,
        AuthenticationService authenticationService, UserMapper userMapper,
        RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.jwtService = jwtService;
      this.authenticationService = authenticationService;
      this.userMapper = userMapper;
      this.refreshTokenService = refreshTokenService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    // Registration page
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute RegisterUserDto request) {
        authenticationService.signup(request);
        return "redirect:/auth/login";
    }

    @PostMapping("/authenticate")
    public String login(@ModelAttribute LoginUserDto request) {
        User authenticatedUser = authenticationService.authenticate(request);

        String jwtToken = jwtService.generateToken(userMapper.toUserDetailsDao(authenticatedUser));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authenticatedUser);
        return "redirect:/auth/oauth2/success";
    }

    @GetMapping("/oauth2/success")
    public String oauth2Success() {
        return "redirect:/success";
    }

    @Getter
    @AllArgsConstructor
    private static class AuthResponse {
        private String token;
    }
}
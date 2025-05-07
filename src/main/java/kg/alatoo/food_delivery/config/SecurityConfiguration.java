package kg.alatoo.food_delivery.config;

import java.util.List;
import kg.alatoo.food_delivery.entity.RefreshToken;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.filter.JwtAuthenticationFilter;
import kg.alatoo.food_delivery.oauth.CustomOAuth2UserService;
import kg.alatoo.food_delivery.repository.UserRepository;
import kg.alatoo.food_delivery.service.security.JWTService;
import kg.alatoo.food_delivery.service.security.RefreshTokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  private final AuthenticationProvider authenticationProvider;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final CustomOAuth2UserService customOAuth2UserService;
  private final JWTService jwtService;
  private final RefreshTokenService refreshTokenService;

  public SecurityConfiguration(
      JwtAuthenticationFilter jwtAuthenticationFilter,
      AuthenticationProvider authenticationProvider,
      CustomOAuth2UserService customOAuth2UserService,
      JWTService jwtService, RefreshTokenService refreshTokenService
  ) {
    this.authenticationProvider = authenticationProvider;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.customOAuth2UserService = customOAuth2UserService;
    this.jwtService = jwtService;
    this.refreshTokenService = refreshTokenService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, UserRepository userRepository)
      throws Exception {
    http.csrf()
        .disable()
        .authorizeHttpRequests()
        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/h2/**", "/oauth2/**",
            "/",
            "/css/**",
            "/js/**",
            "/webjars/**", "/home/**", "/oauth-success", "/login/oauth2/code/google",
            "/login/oauth2/code/github")
        .permitAll()

        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.PATCH, "/api/users/**").hasRole("ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

        .requestMatchers(HttpMethod.POST, "/api/dishes/**").hasAnyRole("RESTAURANT", "ADMIN")
        .requestMatchers(HttpMethod.PUT, "/api/dishes/**").hasAnyRole("RESTAURANT", "ADMIN")
        .requestMatchers(HttpMethod.PATCH, "/api/dishes/**").hasAnyRole("RESTAURANT", "ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/dishes/**").hasAnyRole("RESTAURANT", "ADMIN")

        .requestMatchers(HttpMethod.GET, "/api/orders/**")
        .hasAnyRole("CLIENT", "RESTAURANT", "ADMIN")
        .requestMatchers(HttpMethod.POST, "/api/orders").hasRole("CLIENT")
        .requestMatchers(HttpMethod.PUT, "/api/orders/**").hasAnyRole("RESTAURANT", "ADMIN")
        .requestMatchers(HttpMethod.PATCH, "/api/orders/**").hasAnyRole("COURIER", "ADMIN")
        .requestMatchers(HttpMethod.DELETE, "/api/orders/**").hasRole("ADMIN")

        // orders
        .requestMatchers("/api/orders").hasAnyRole("CLIENT", "RESTAURANT")
        .requestMatchers("/api/orders/**").hasAnyRole("CLIENT", "RESTAURANT", "COURIER")
        .requestMatchers(HttpMethod.POST, "/api/orders").hasRole("CLIENT")
        .requestMatchers(HttpMethod.PUT, "/api/orders/**").hasRole("RESTAURANT")
        .requestMatchers(HttpMethod.PATCH, "/api/orders/**/status").hasRole("COURIER")

        // restaurants
        .requestMatchers(HttpMethod.POST, "/api/restaurants").hasRole("ADMIN")
        .requestMatchers(HttpMethod.GET, "/api/restaurants").permitAll()

        .requestMatchers(HttpMethod.PUT, "/api/restaurants/**").hasRole("RESTAURANT")
        .requestMatchers(HttpMethod.DELETE, "/api/restaurants/**").hasRole("ADMIN")

        // dishes
        .requestMatchers(HttpMethod.POST, "/api/dishes").hasRole("RESTAURANT")
        .requestMatchers(HttpMethod.PUT, "/api/dishes/**").hasRole("RESTAURANT")
        .requestMatchers(HttpMethod.DELETE, "/api/dishes/**").hasRole("RESTAURANT")

        .anyRequest()
        .authenticated()
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .headers(headers -> headers
            .frameOptions(frame -> frame
                .sameOrigin()
            )
        )
        .oauth2Login(oauth2 -> oauth2
            .loginPage("/auth/login")
            .userInfoEndpoint(userInfo -> userInfo
                .userService(customOAuth2UserService)
            )
            .successHandler((request, response, authentication) -> {
              String username;
              if (authentication.getPrincipal() instanceof DefaultOidcUser oidcUser) {
                username = oidcUser.getEmail();
              } else if (authentication.getPrincipal() instanceof DefaultOAuth2User oauth2User) {
                username = oauth2User.getAttribute("email");
              } else {
                username = authentication.getName();
              }

              User user = userRepository.findByUsername(username)
                  .orElseThrow(() -> new UsernameNotFoundException("User not found"));

              String jwt = jwtService.generateToken(user);
              RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

              response.addHeader("Authorization", "Bearer " + jwt);
              response.sendRedirect("/");
            })
            .failureHandler((request, response, exception) -> {
              System.out.println("OAuth2 Login Failed: " + exception.getMessage());
              response.sendRedirect("/auth/login?error");
            })
        );
    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(List.of("http://localhost:8005"));
    configuration.setAllowedMethods(List.of("GET", "POST"));
    configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
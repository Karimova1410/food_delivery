package kg.alatoo.food_delivery.config;

import java.util.List;
import kg.alatoo.food_delivery.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
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

  public SecurityConfiguration(
      JwtAuthenticationFilter jwtAuthenticationFilter,
      AuthenticationProvider authenticationProvider
  ) {
    this.authenticationProvider = authenticationProvider;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .authorizeHttpRequests()
        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/h2/**")
        .permitAll()
        //users
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

        .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("CLIENT", "RESTAURANT", "ADMIN")
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
        );;

    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOrigins(List.of("http://localhost:8005"));
    configuration.setAllowedMethods(List.of("GET","POST"));
    configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**",configuration);

    return source;
  }
}
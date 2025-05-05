package kg.alatoo.food_delivery.service.security;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import kg.alatoo.food_delivery.entity.RefreshToken;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.exception.TokenRefreshException;
import kg.alatoo.food_delivery.repository.RefreshTokenRepository;
import kg.alatoo.food_delivery.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {
  @Value("${security.jwt.refresh-expiration-time}")
  private Long refreshTokenDurationMs;

  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;

  public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
    this.userRepository = userRepository;
  }

  public Optional<RefreshToken> findByToken(String token) {
    try {
      UUID uuid = UUID.fromString(token);
      return refreshTokenRepository.findByToken(uuid);
    } catch (IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  public RefreshToken createRefreshToken(User user) {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(user);
    refreshToken.setToken(UUID.randomUUID());
    refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
    return refreshTokenRepository.save(refreshToken);
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException(token.getToken().toString(), "Refresh token was expired. Please make a new signin request");
    }
    return token;
  }

  @Transactional
  public void deleteByUserId(Long userId) {
    userRepository.findById(userId).ifPresent(refreshTokenRepository::deleteByUser);
  }
}
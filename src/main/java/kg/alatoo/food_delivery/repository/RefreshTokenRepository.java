package kg.alatoo.food_delivery.repository;

import java.util.Optional;
import java.util.UUID;
import kg.alatoo.food_delivery.entity.RefreshToken;
import kg.alatoo.food_delivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken(UUID token);

  Optional<RefreshToken> findByUser(User user);

  @Modifying
  int deleteByUser(User user);
}
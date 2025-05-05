package kg.alatoo.food_delivery.repository;

import java.util.Optional;
import kg.alatoo.food_delivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
  Optional<User> findById(Long id);
}

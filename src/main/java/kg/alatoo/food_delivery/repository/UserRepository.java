package kg.alatoo.food_delivery.repository;

import kg.alatoo.food_delivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}

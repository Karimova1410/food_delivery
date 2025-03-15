package kg.alatoo.food_delivery.repository;

import kg.alatoo.food_delivery.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

}

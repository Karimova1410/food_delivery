package kg.alatoo.food_delivery.repository;

import kg.alatoo.food_delivery.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {

}

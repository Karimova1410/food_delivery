package kg.alatoo.food_delivery.repository;

import kg.alatoo.food_delivery.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}

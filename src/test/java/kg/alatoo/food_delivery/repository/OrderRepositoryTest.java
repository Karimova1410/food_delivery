package kg.alatoo.food_delivery.repository;

import kg.alatoo.food_delivery.entity.Order;
import kg.alatoo.food_delivery.entity.Restaurant;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.enums.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class OrderRepositoryTest {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RestaurantRepository restaurantRepository;

  @Test
  void testSaveOrder() {
    User client = userRepository.save(User.builder().username("client1").build());
    Restaurant restaurant = restaurantRepository.save(Restaurant.builder().name("Pizza Place").build());

    Order order = Order.builder()
        .client(client)
        .restaurant(restaurant)
        .status(OrderStatus.PROCESSING)
        .build();

    Order savedOrder = orderRepository.save(order);

    assertNotNull(savedOrder.getId());
    assertEquals(client.getId(), savedOrder.getClient().getId());
    assertEquals(restaurant.getId(), savedOrder.getRestaurant().getId());
    assertEquals(OrderStatus.PROCESSING, savedOrder.getStatus());
  }

  @Test
  void testFindById() {
    User client = userRepository.save(User.builder().username("client2").build());
    Restaurant restaurant = restaurantRepository.save(Restaurant.builder().name("Burger House").build());

    Order order = Order.builder()
        .client(client)
        .restaurant(restaurant)
        .status(OrderStatus.CREATED)
        .build();
    Order savedOrder = orderRepository.save(order);

    Optional<Order> foundOrder = orderRepository.findById(savedOrder.getId());

    assertTrue(foundOrder.isPresent());
    assertEquals(OrderStatus.CREATED, foundOrder.get().getStatus());
  }

  @Test
  void testUpdateOrderStatus() {
    User client = userRepository.save(User.builder().username("client3").build());
    Restaurant restaurant = restaurantRepository.save(Restaurant.builder().name("Sushi World").build());

    Order order = Order.builder()
        .client(client)
        .restaurant(restaurant)
        .status(OrderStatus.PROCESSING)
        .build();
    Order savedOrder = orderRepository.save(order);

    savedOrder.setStatus(OrderStatus.COMPLETED);
    Order updatedOrder = orderRepository.save(savedOrder);

    assertEquals(OrderStatus.COMPLETED, updatedOrder.getStatus());
  }

  @Test
  void testDeleteOrder() {
    User client = userRepository.save(User.builder().username("client4").build());
    Restaurant restaurant = restaurantRepository.save(Restaurant.builder().name("Taco Bell").build());

    Order order = Order.builder()
        .client(client)
        .restaurant(restaurant)
        .status(OrderStatus.COMPLETED)
        .build();
    Order savedOrder = orderRepository.save(order);

    orderRepository.deleteById(savedOrder.getId());
    Optional<Order> deletedOrder = orderRepository.findById(savedOrder.getId());

    assertFalse(deletedOrder.isPresent());
  }
}

package kg.alatoo.food_delivery.repository;

import kg.alatoo.food_delivery.entity.Restaurant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class RestaurantRepositoryTest {

  @Autowired
  private RestaurantRepository restaurantRepository;

  @Test
  void testSaveRestaurant() {
    Restaurant restaurant = Restaurant.builder()
        .name("Pizza House")
        .address("123 Main St")
        .build();

    Restaurant savedRestaurant = restaurantRepository.save(restaurant);

    assertNotNull(savedRestaurant.getId());
    assertEquals("Pizza House", savedRestaurant.getName());
    assertEquals("123 Main St", savedRestaurant.getAddress());
  }

  @Test
  void testFindById() {
    Restaurant restaurant = Restaurant.builder()
        .name("Burger King")
        .address("456 Elm St")
        .build();
    Restaurant savedRestaurant = restaurantRepository.save(restaurant);

    Optional<Restaurant> foundRestaurant = restaurantRepository.findById(savedRestaurant.getId());

    assertTrue(foundRestaurant.isPresent());
    assertEquals("Burger King", foundRestaurant.get().getName());
  }

  @Test
  void testUpdateRestaurant() {
    Restaurant restaurant = Restaurant.builder()
        .name("Sushi World")
        .address("789 Oak St")
        .build();
    Restaurant savedRestaurant = restaurantRepository.save(restaurant);

    savedRestaurant.setName("Updated Sushi World");
    savedRestaurant.setAddress("999 Pine St");
    Restaurant updatedRestaurant = restaurantRepository.save(savedRestaurant);

    assertEquals(savedRestaurant.getId(), updatedRestaurant.getId());
    assertEquals("Updated Sushi World", updatedRestaurant.getName());
    assertEquals("999 Pine St", updatedRestaurant.getAddress());
  }

  @Test
  void testDeleteRestaurant() {
    Restaurant restaurant = Restaurant.builder()
        .name("Taco Bell")
        .address("111 Maple St")
        .build();
    Restaurant savedRestaurant = restaurantRepository.save(restaurant);

    restaurantRepository.deleteById(savedRestaurant.getId());
    Optional<Restaurant> deletedRestaurant = restaurantRepository.findById(savedRestaurant.getId());

    assertFalse(deletedRestaurant.isPresent());
  }
}

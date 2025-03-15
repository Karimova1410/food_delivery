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
        .name("Ala-too")
        .address("Ankara 8/1")
        .build();

    Restaurant savedRestaurant = restaurantRepository.save(restaurant);

    assertNotNull(savedRestaurant.getId());
    assertEquals("Ala-too", savedRestaurant.getName());
    assertEquals("Ankara 8/1", savedRestaurant.getAddress());
  }

  @Test
  void testFindById() {
    Restaurant restaurant = Restaurant.builder()
        .name("Ala-too")
        .address("Ankara 8/1")
        .build();
    Restaurant savedRestaurant = restaurantRepository.save(restaurant);

    Optional<Restaurant> foundRestaurant = restaurantRepository.findById(savedRestaurant.getId());

    assertTrue(foundRestaurant.isPresent());
    assertEquals("Ala-too", foundRestaurant.get().getName());
  }

  @Test
  void testUpdateRestaurant() {
    Restaurant restaurant = Restaurant.builder()
        .name("Ala-too")
        .address("Ankara 8/1")
        .build();
    Restaurant savedRestaurant = restaurantRepository.save(restaurant);

    savedRestaurant.setName("Updated Ala-too");
    savedRestaurant.setAddress("Ankara 8/2");
    Restaurant updatedRestaurant = restaurantRepository.save(savedRestaurant);

    assertEquals(savedRestaurant.getId(), updatedRestaurant.getId());
    assertEquals("Updated Ala-too", updatedRestaurant.getName());
    assertEquals("Ankara 8/2", updatedRestaurant.getAddress());
  }

  @Test
  void testDeleteRestaurant() {
    Restaurant restaurant = Restaurant.builder()
        .name("Ala-too")
        .address("Ankara 8/2")
        .build();
    Restaurant savedRestaurant = restaurantRepository.save(restaurant);

    restaurantRepository.deleteById(savedRestaurant.getId());
    Optional<Restaurant> deletedRestaurant = restaurantRepository.findById(savedRestaurant.getId());

    assertFalse(deletedRestaurant.isPresent());
  }
}

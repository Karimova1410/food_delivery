package kg.alatoo.food_delivery.repository;

import kg.alatoo.food_delivery.entity.Dish;
import kg.alatoo.food_delivery.entity.Restaurant;
import kg.alatoo.food_delivery.enums.DishCategory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class DishRepositoryTest {

  @Autowired
  private DishRepository dishRepository;

  @Autowired
  private RestaurantRepository restaurantRepository;

  @Test
  void testSaveDish() {
    Restaurant restaurant = restaurantRepository.save(Restaurant.builder().name("Pizza Place").build());

    Dish dish = Dish.builder()
        .name("Margherita Pizza")
        .description("Classic pizza with tomato sauce and cheese")
        .price(10.99)
        .category(DishCategory.MAIN_COURSE)
        .restaurant(restaurant)
        .build();

    Dish savedDish = dishRepository.save(dish);

    assertNotNull(savedDish.getId());
    assertEquals("Margherita Pizza", savedDish.getName());
    assertEquals(10.99, savedDish.getPrice());
    assertEquals(DishCategory.MAIN_COURSE, savedDish.getCategory());
    assertEquals(restaurant.getId(), savedDish.getRestaurant().getId());
  }

  @Test
  void testFindById() {
    Restaurant restaurant = restaurantRepository.save(Restaurant.builder().name("Burger House").build());

    Dish dish = Dish.builder()
        .name("Cheeseburger")
        .description("Beef patty with cheese, lettuce, and tomato")
        .price(7.99)
        .category(DishCategory.APPETIZER)
        .restaurant(restaurant)
        .build();
    Dish savedDish = dishRepository.save(dish);

    Optional<Dish> foundDish = dishRepository.findById(savedDish.getId());

    assertTrue(foundDish.isPresent());
    assertEquals("Cheeseburger", foundDish.get().getName());
  }

  @Test
  void testUpdateDish() {
    Restaurant restaurant = restaurantRepository.save(Restaurant.builder().name("Sushi World").build());

    Dish dish = Dish.builder()
        .name("Salmon Sushi")
        .description("Fresh salmon with rice and seaweed")
        .price(12.99)
        .category(DishCategory.APPETIZER)
        .restaurant(restaurant)
        .build();
    Dish savedDish = dishRepository.save(dish);

    savedDish.setPrice(13.99);
    savedDish.setName("Updated Salmon Sushi");
    Dish updatedDish = dishRepository.save(savedDish);

    assertEquals(savedDish.getId(), updatedDish.getId());
    assertEquals("Updated Salmon Sushi", updatedDish.getName());
    assertEquals(13.99, updatedDish.getPrice());
  }

  @Test
  void testDeleteDish() {
    Restaurant restaurant = restaurantRepository.save(Restaurant.builder().name("Taco Bell").build());

    Dish dish = Dish.builder()
        .name("Taco")
        .description("Crispy taco with beef and cheese")
        .price(5.99)
        .category(DishCategory.APPETIZER)
        .restaurant(restaurant)
        .build();
    Dish savedDish = dishRepository.save(dish);

    dishRepository.deleteById(savedDish.getId());
    Optional<Dish> deletedDish = dishRepository.findById(savedDish.getId());

    assertFalse(deletedDish.isPresent());
  }
}

package kg.alatoo.food_delivery.bootstrap;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.transaction.Transactional;
import java.io.FileReader;
import java.io.IOException;
import kg.alatoo.food_delivery.entity.Dish;
import kg.alatoo.food_delivery.entity.Order;
import kg.alatoo.food_delivery.entity.Restaurant;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.enums.DishCategory;
import kg.alatoo.food_delivery.enums.OrderStatus;
import kg.alatoo.food_delivery.enums.UserRole;
import kg.alatoo.food_delivery.repository.DishRepository;
import kg.alatoo.food_delivery.repository.OrderRepository;
import kg.alatoo.food_delivery.repository.RestaurantRepository;
import kg.alatoo.food_delivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class BootstrapDataLoader implements CommandLineRunner {

  private final UserRepository userRepository;
  private final RestaurantRepository restaurantRepository;
  private final DishRepository dishRepository;
  private final OrderRepository orderRepository;

  @Override
  @Transactional
  public void run(String... args) throws Exception {
    loadUsersFromCsv();
    loadRestaurantsFromCsv();
    loadDishesFromCsv();
    loadOrdersFromCsv();
  }

  private void loadUsersFromCsv() {
    String csvFile = "src/main/resources/users.csv";

    try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
      String[] line;
      boolean isHeader = true;

      while ((line = reader.readNext()) != null) {
        if (isHeader) {
          isHeader = false;
          continue;
        }

        User user = new User();
        user.setUsername(line[0]);
        user.setPassword(line[1]);
        user.setRole(UserRole.valueOf(line[2]));

        userRepository.save(user);
      }
    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    }
  }

  private void loadRestaurantsFromCsv() {
    String csvFile = "src/main/resources/restaurants.csv";

    try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
      String[] line;
      boolean isHeader = true;

      while ((line = reader.readNext()) != null) {
        if (isHeader) {
          isHeader = false;
          continue;
        }

        Restaurant restaurant = new Restaurant();
        restaurant.setName(line[0]);
        restaurant.setAddress(line[1]);

        restaurantRepository.save(restaurant);
      }
    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    }
  }

  private void loadDishesFromCsv() {
    String csvFile = "src/main/resources/dishes.csv";

    try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
      String[] line;
      boolean isHeader = true;

      while ((line = reader.readNext()) != null) {
        if (isHeader) {
          isHeader = false;
          continue;
        }

        Dish dish = new Dish();
        dish.setName(line[0]);
        dish.setDescription(line[1]);
        dish.setPrice(Double.parseDouble(line[2]));
        dish.setCategory(DishCategory.valueOf(line[3]));

        Long restaurantId = Long.parseLong(line[4]);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        dish.setRestaurant(restaurant);

        dishRepository.save(dish);
      }
    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    }
  }

  private void loadOrdersFromCsv() {
    String csvFile = "src/main/resources/orders.csv";

    try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
      String[] line;
      boolean isHeader = true;

      while ((line = reader.readNext()) != null) {
        if (isHeader) {
          isHeader = false;
          continue;
        }

        Order order = new Order();

        Long clientId = Long.parseLong(line[0]);
        User client = userRepository.findById(clientId)
            .orElseThrow(() -> new RuntimeException("Client not found"));
        order.setClient(client);

        Long restaurantId = Long.parseLong(line[1]);
        Restaurant restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        order.setRestaurant(restaurant);

        order.setStatus(OrderStatus.valueOf(line[2]));

        Long courierId = Long.parseLong(line[3]);
        User courier = userRepository.findById(courierId)
            .orElseThrow(() -> new RuntimeException("Courier not found"));
        order.setCourier(courier);

        orderRepository.save(order);
      }
    } catch (IOException | CsvValidationException e) {
      e.printStackTrace();
    }
  }
}
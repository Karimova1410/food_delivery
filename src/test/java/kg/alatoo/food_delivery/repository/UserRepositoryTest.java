package kg.alatoo.food_delivery.repository;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Test
  void testSaveUser() {
    User user = new User();
    user.setUsername("testuser");
    user.setPassword("password123");
    user.setRole(UserRole.ADMIN);

    User savedUser = userRepository.save(user);

    assertNotNull(savedUser.getId());
    assertEquals("testuser", savedUser.getUsername());
  }

  @Test
  void testFindById() {
    User user = new User();
    user.setUsername("john_doe");
    user.setPassword("securepass");
    user.setRole(UserRole.ADMIN);
    User savedUser = userRepository.save(user);

    Optional<User> foundUser = userRepository.findById(savedUser.getId());

    assertTrue(foundUser.isPresent());
    assertEquals("john_doe", foundUser.get().getUsername());
  }

  @Test
  void testDeleteUser() {
    User user = new User();
    user.setUsername("tempuser");
    user.setPassword("temp123");
    user.setRole(UserRole.ADMIN);
    User savedUser = userRepository.save(user);

    userRepository.deleteById(savedUser.getId());
    Optional<User> deletedUser = userRepository.findById(savedUser.getId());

    assertFalse(deletedUser.isPresent());
  }

  @Test
  void testUpdateUser() {
    User user = new User();
    user.setUsername("originalUser");
    user.setPassword("originalPass");
    user.setRole(UserRole.ADMIN);
    User savedUser = userRepository.save(user);

    savedUser.setUsername("updatedUser");
    savedUser.setPassword("updatedPass");
    user.setRole(UserRole.ADMIN);
    User updatedUser = userRepository.save(savedUser);

    assertEquals(savedUser.getId(), updatedUser.getId());
    assertEquals("updatedUser", updatedUser.getUsername());
    assertEquals("updatedPass", updatedUser.getPassword());
    assertEquals(UserRole.ADMIN, updatedUser.getRole());
  }

}
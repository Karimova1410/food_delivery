package kg.alatoo.food_delivery.mapper;

import kg.alatoo.food_delivery.dao.UserDetailsDao;
import kg.alatoo.food_delivery.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDetailsDao toUserDetailsDao(User user) {
        return new UserDetailsDao(user);
    }
}

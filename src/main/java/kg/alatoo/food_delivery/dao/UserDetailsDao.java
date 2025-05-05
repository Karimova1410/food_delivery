package kg.alatoo.food_delivery.dao;

import java.util.Collection;
import java.util.Collections;
import kg.alatoo.food_delivery.entity.User;
import kg.alatoo.food_delivery.enums.Role;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Data
public class UserDetailsDao implements UserDetails {


    private String name;
    private String surname;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;


    public UserDetailsDao(User user) {
        username = user.getEmail();
        password = user.getPassword();
        name = user.getName();
        surname = user.getSurname();
        Role role = user.getRole();
        authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

package kg.alatoo.food_delivery.dto.auth;

import kg.alatoo.food_delivery.enums.Role;
import lombok.Data;

@Data
public class RegisterUserDto {
  private String email;
  private String password;
  private String name;
  private String surname;
  private Role role;
  private String username;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }


  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  @Override
  public String toString() {
    return "RegisterUserDto{" +
        "email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", name='" + name + '\'' +
        ", surname='" + surname + '\'' +
        ", role=" + role +
        '}';
  }
}

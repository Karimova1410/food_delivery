package kg.alatoo.food_delivery.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kg.alatoo.food_delivery.enums.DishCategory;
import lombok.Data;

@Data
@Entity
public class Dish {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private double price;

  @Enumerated(EnumType.STRING)
  private DishCategory category;
  @ManyToOne
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;
}

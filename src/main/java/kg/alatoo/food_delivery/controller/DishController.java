package kg.alatoo.food_delivery.controller;

import kg.alatoo.food_delivery.dto.dish.DishRequestDto;
import kg.alatoo.food_delivery.dto.dish.DishResponseDto;
import kg.alatoo.food_delivery.service.DishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

  private final DishService dishService;

  public DishController(DishService dishService) {
    this.dishService = dishService;
  }

  @GetMapping
  public ResponseEntity<List<DishResponseDto>> getAllDishes() {
    List<DishResponseDto> dishes = dishService.findAll();
    return new ResponseEntity<>(dishes, HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<DishResponseDto> getDishById(@PathVariable Long id) {
    DishResponseDto dish = dishService.findById(id);
    return new ResponseEntity<>(dish, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<DishResponseDto> createDish(@RequestBody DishRequestDto dishRequestDto) {
    DishResponseDto createdDish = dishService.createDish(dishRequestDto);
    return new ResponseEntity<>(createdDish, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<DishResponseDto> updateDish(
      @PathVariable Long id,
      @RequestBody DishRequestDto dishRequestDto) {
    DishResponseDto updatedDish = dishService.updateDish(id, dishRequestDto);
    return new ResponseEntity<>(updatedDish, HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<DishResponseDto> patchDish(
      @PathVariable Long id,
      @RequestBody DishRequestDto dishRequestDto) {
    DishResponseDto patchedDish = dishService.patchDish(id, dishRequestDto);
    return new ResponseEntity<>(patchedDish, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
    dishService.deleteDish(id);
    return ResponseEntity.noContent().build();
  }
}
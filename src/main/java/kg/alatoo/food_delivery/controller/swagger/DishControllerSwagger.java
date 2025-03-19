package kg.alatoo.food_delivery.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.alatoo.food_delivery.dto.dish.DishRequestDto;
import kg.alatoo.food_delivery.dto.dish.DishResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Dish Controller", description = "APIs for managing dishes")
public interface DishControllerSwagger {

  @Operation(summary = "Get all dishes", description = "Retrieve a list of all dishes")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved list of dishes",
      content = @Content(schema = @Schema(implementation = DishResponseDto.class)))
  ResponseEntity<List<DishResponseDto>> getAllDishes();

  @Operation(summary = "Get dish by ID", description = "Retrieve a dish by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved dish",
          content = @Content(schema = @Schema(implementation = DishResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Dish not found")
  })
  ResponseEntity<DishResponseDto> getDishById(
      @Parameter(description = "ID of the dish to be retrieved", required = true)
      Long id);

  @Operation(summary = "Create a new dish", description = "Create a new dish with the provided details")
  @ApiResponse(responseCode = "201", description = "Dish created successfully",
      content = @Content(schema = @Schema(implementation = DishResponseDto.class)))
  ResponseEntity<DishResponseDto> createDish(
      @Parameter(description = "Dish details to create", required = true)
      DishRequestDto dishRequestDto);

  @Operation(summary = "Update a dish", description = "Update an existing dish by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Dish updated successfully",
          content = @Content(schema = @Schema(implementation = DishResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Dish not found")
  })
  ResponseEntity<DishResponseDto> updateDish(
      @Parameter(description = "ID of the dish to be updated", required = true)
      Long id,
      @Parameter(description = "Updated dish details", required = true)
      DishRequestDto dishRequestDto);

  @Operation(summary = "Partially update a dish", description = "Partially update an existing dish by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Dish patched successfully",
          content = @Content(schema = @Schema(implementation = DishResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Dish not found")
  })
  ResponseEntity<DishResponseDto> patchDish(
      @Parameter(description = "ID of the dish to be patched", required = true)
      Long id,
      @Parameter(description = "Partial dish details to update", required = true)
      DishRequestDto dishRequestDto);

  @Operation(summary = "Delete a dish", description = "Delete a dish by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Dish deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Dish not found")
  })
  ResponseEntity<Void> deleteDish(
      @Parameter(description = "ID of the dish to be deleted", required = true)
      Long id);
}

package kg.alatoo.food_delivery.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantRequestDto;
import kg.alatoo.food_delivery.dto.restaurant.RestaurantResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Restaurant Controller", description = "APIs for managing restaurants")
public interface RestaurantControllerSwagger {

  @Operation(summary = "Create a new restaurant", description = "Create a new restaurant with the provided details")
  @ApiResponse(responseCode = "201", description = "Restaurant created successfully",
      content = @Content(schema = @Schema(implementation = RestaurantResponseDto.class)))
  ResponseEntity<RestaurantResponseDto> createRestaurant(
      @Parameter(description = "Restaurant details to create", required = true)
      @Valid @RequestBody RestaurantRequestDto restaurantRequestDto);

  @Operation(summary = "Get restaurant by ID", description = "Retrieve a restaurant by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved restaurant",
          content = @Content(schema = @Schema(implementation = RestaurantResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Restaurant not found")
  })
  ResponseEntity<RestaurantResponseDto> getRestaurantById(
      @Parameter(description = "ID of the restaurant to be retrieved", required = true)
      Long id);

  @Operation(summary = "Get all restaurants", description = "Retrieve a list of all restaurants")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved list of restaurants",
      content = @Content(schema = @Schema(implementation = RestaurantResponseDto.class)))
  ResponseEntity<List<RestaurantResponseDto>> getAllRestaurants();

  @Operation(summary = "Update a restaurant", description = "Update an existing restaurant by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Restaurant updated successfully",
          content = @Content(schema = @Schema(implementation = RestaurantResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Restaurant not found")
  })
  ResponseEntity<RestaurantResponseDto> updateRestaurant(
      @Parameter(description = "ID of the restaurant to be updated", required = true)
      Long id,
      @Parameter(description = "Updated restaurant details", required = true)
      @Valid @RequestBody RestaurantRequestDto restaurantRequestDto);

  @Operation(summary = "Delete a restaurant", description = "Delete a restaurant by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Restaurant deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Restaurant not found")
  })
  ResponseEntity<Void> deleteRestaurant(
      @Parameter(description = "ID of the restaurant to be deleted", required = true)
      Long id);

  @Operation(summary = "Partially update a restaurant", description = "Partially update an existing restaurant by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Restaurant patched successfully",
          content = @Content(schema = @Schema(implementation = RestaurantResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Restaurant not found")
  })
  ResponseEntity<RestaurantResponseDto> patchRestaurant(
      @Parameter(description = "ID of the restaurant to be patched", required = true)
      Long id,
      @Parameter(description = "Partial restaurant details to update", required = true)
      @Valid @RequestBody RestaurantRequestDto restaurantRequestDto);
}
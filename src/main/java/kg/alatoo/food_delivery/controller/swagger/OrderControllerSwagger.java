package kg.alatoo.food_delivery.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kg.alatoo.food_delivery.dto.order.OrderRequestDto;
import kg.alatoo.food_delivery.dto.order.OrderResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Order Controller", description = "APIs for managing orders")
public interface OrderControllerSwagger {

  @Operation(summary = "Get all orders", description = "Retrieve a list of all orders")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved list of orders",
      content = @Content(schema = @Schema(implementation = OrderResponseDto.class)))
  ResponseEntity<List<OrderResponseDto>> getAllOrders();

  @Operation(summary = "Get order by ID", description = "Retrieve an order by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successfully retrieved order",
          content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Order not found")
  })
  ResponseEntity<OrderResponseDto> getOrderById(
      @Parameter(description = "ID of the order to be retrieved", required = true)
      Long id);

  @Operation(summary = "Create a new order", description = "Create a new order with the provided details")
  @ApiResponse(responseCode = "201", description = "Order created successfully",
      content = @Content(schema = @Schema(implementation = OrderResponseDto.class)))
  ResponseEntity<OrderResponseDto> createOrder(
      @Parameter(description = "Order details to create", required = true)
      OrderRequestDto orderRequestDto);

  @Operation(summary = "Update an order", description = "Update an existing order by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Order updated successfully",
          content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Order not found")
  })
  ResponseEntity<OrderResponseDto> updateOrder(
      @Parameter(description = "ID of the order to be updated", required = true)
      Long id,
      @Parameter(description = "Updated order details", required = true)
      OrderRequestDto orderRequestDto);

  @Operation(summary = "Partially update an order", description = "Partially update an existing order by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Order patched successfully",
          content = @Content(schema = @Schema(implementation = OrderResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Order not found")
  })
  ResponseEntity<OrderResponseDto> patchOrder(
      @Parameter(description = "ID of the order to be patched", required = true)
      Long id,
      @Parameter(description = "Partial order details to update", required = true)
      OrderRequestDto orderRequestDto);

  @Operation(summary = "Delete an order", description = "Delete an order by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
      @ApiResponse(responseCode = "404", description = "Order not found")
  })
  ResponseEntity<Void> deleteOrder(
      @Parameter(description = "ID of the order to be deleted", required = true)
      Long id);
}
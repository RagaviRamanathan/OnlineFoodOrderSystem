package com.example.demo;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class FoodOrderController {

    private final FoodOrderService service;
    private final Validator validator;

    public FoodOrderController(
            FoodOrderService service,
            Validator validator) {
        this.service = service;
        this.validator = validator;
    }

    // GET: All orders
    @GetMapping
    public List<FoodOrder> getAllOrders() {
        return service.getAllOrders();
    }

    // GET: Order by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        FoodOrder order = service.getOrderById(id);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order not found with ID: " + id);
        }

        return ResponseEntity.ok(order);
    }

    // POST: Add order
    @PostMapping
    public ResponseEntity<FoodOrder> addOrder(
            @Valid @RequestBody FoodOrder order) {
        FoodOrder saved = service.addOrder(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT: Update complete order
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody FoodOrder updatedOrder) {

        FoodOrder result = service.updateOrder(id, updatedOrder);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order not found with ID: " + id);
        }

        return ResponseEntity.ok(result);
    }

    // DELETE: Delete order
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        boolean deleted = service.deleteOrder(id);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Order not found with ID: " + id);
        }

        return ResponseEntity.ok("Order deleted successfully");
    }

    // PATCH: Update selected fields
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchOrder(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        try {
            FoodOrder updated = service.patchOrder(id, updates);

            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Order not found with ID: " + id);
            }

            var errors = validator.validate(updated);

            if (!errors.isEmpty()) {
                var messages = errors.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.toList());

                return ResponseEntity.badRequest().body(messages);
            }

            return ResponseEntity.ok(updated);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}

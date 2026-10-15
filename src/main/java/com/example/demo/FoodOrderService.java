
package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class FoodOrderService {

    private final List<FoodOrder> orders = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Get all orders
    public List<FoodOrder> getAllOrders() {
        return orders;
    }

    // Get order by ID
    public FoodOrder getOrderById(Long id) {
        return orders.stream()
                .filter(order -> order.getOrderId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // Add order
    public FoodOrder addOrder(FoodOrder order) {
        order.setOrderId(idGenerator.getAndIncrement());
        orders.add(order);
        return order;
    }

    // Update complete order
    public FoodOrder updateOrder(Long id, FoodOrder updatedOrder) {
        FoodOrder existing = getOrderById(id);

        if (existing == null) {
            return null;
        }

        updatedOrder.setOrderId(id);
        orders.set(orders.indexOf(existing), updatedOrder);

        return updatedOrder;
    }

    // Delete order
    public boolean deleteOrder(Long id) {
        return orders.removeIf(
                order -> order.getOrderId().equals(id));
    }

    // Partial update
    public FoodOrder patchOrder(Long id, Map<String, Object> updates) {
        FoodOrder existing = getOrderById(id);

        if (existing == null) {
            return null;
        }

        // Make a copy so invalid updates do not change existing data
        FoodOrder updated = new FoodOrder();
        updated.setOrderId(existing.getOrderId());
        updated.setCustomerName(existing.getCustomerName());
        updated.setFoodItem(existing.getFoodItem());
        updated.setQuantity(existing.getQuantity());
        updated.setPrice(existing.getPrice());

        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            switch (key) {
                case "customerName":
                    if (!(value instanceof String)) {
                        throw new IllegalArgumentException(
                                "Customer name must be text");
                    }
                    updated.setCustomerName((String) value);
                    break;

                case "foodItem":
                    if (!(value instanceof String)) {
                        throw new IllegalArgumentException(
                                "Food item must be text");
                    }
                    updated.setFoodItem((String) value);
                    break;

                case "quantity":
                    if (!(value instanceof Number)) {
                        throw new IllegalArgumentException(
                                "Quantity must be a number");
                    }
                    double quantityValue =
                            ((Number) value).doubleValue();

                    if (quantityValue != Math.floor(quantityValue)
                            || quantityValue > Integer.MAX_VALUE
                            || quantityValue < Integer.MIN_VALUE) {
                        throw new IllegalArgumentException(
                                "Quantity must be a whole number");
                    }

                    updated.setQuantity((int) quantityValue);
                    break;

                case "price":
                    if (!(value instanceof Number)) {
                        throw new IllegalArgumentException(
                                "Price must be a number");
                    }
                    updated.setPrice(
                            new java.math.BigDecimal(value.toString()));
                    break;

                case "orderId":
                    throw new IllegalArgumentException(
                            "Order ID cannot be changed");

                default:
                    throw new IllegalArgumentException(
                            "Unknown field: " + key);
            }
        }

        // Validation is handled by the controller.
        orders.set(orders.indexOf(existing), updated);
        return updated;
    }
}

package org.example.itech_heaven.Service;


import org.example.itech_heaven.Entity.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<Order> getOrders();
    List<Order> getOrdersByUserName(String name);
    List<Order> getOrdersByDate(LocalDate date);
    List<Order> getOrdersByDateAndName(String name, LocalDate date);
    List<Order> getAllOrders();
    Order getOrderById(int id);
    void deleteOrder(int orderId);
    void deleteOrdersById(List<Integer> orderIds);
    void updateOrder(Order order);
    List<Order> getOrdersByStatus(String status);
    long countOrders();

    Order addOrder(Order order, int[] cartIds);

    Order addOrderPending(Order order, int[] cartIds);

    List<Order> getOrderByUserId(int id);
}

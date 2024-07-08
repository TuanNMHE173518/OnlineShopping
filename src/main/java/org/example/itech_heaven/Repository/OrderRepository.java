package org.example.itech_heaven.Repository;

import org.example.itech_heaven.Entity.Order;
import org.example.itech_heaven.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByDate(LocalDate date);
    void deleteById(int id);
    List<Order> findByStatus(String status);

    List<Order> findOrdersByUserId(int id);

}

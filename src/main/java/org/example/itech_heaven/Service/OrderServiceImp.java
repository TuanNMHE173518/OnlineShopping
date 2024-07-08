package org.example.itech_heaven.Service;

import org.example.itech_heaven.Entity.Order;
import org.example.itech_heaven.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.*;
import org.example.itech_heaven.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements  OrderService{
    private final OrderDetailRepository orderDetailRepository;
    private final CartDetailsRepository cartDetailsRepository;
    private final DeviceRepository deviceRepository;
    private final AccessoryRepository accessoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> getOrders() {
        List<Order> orders = orderRepository.findAll();
        Collections.reverse(orders);
        return orders;
    }

    @Override
    public List<Order> getOrdersByUserName(String name) {
        List<Order> orders = new ArrayList<>();
        for (Order order : getOrders()) {
            if (order.getUser().getLastname().contains(name)) {
                orders.add(order);
            }
        }
        return orders;
    }

    @Override
    public List<Order> getOrdersByDate(LocalDate date) {
        return orderRepository.findAllByDate(date);
    }

    @Override
    public List<Order> getOrdersByDateAndName(String name, LocalDate date) {
        List<Order> orders = new ArrayList<>();
        for (Order order : getOrdersByDate(date)) {
            if (order.getUser().getLastname().contains(name)) {
                orders.add(order);
            }
        }
        return orders;
    }
        @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderById(int id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteOrder(int orderId) {
        orderRepository.deleteById(orderId);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public long countOrders() {
        return orderRepository.count();
    }

    @Override
    public void deleteOrdersById(List<Integer> orderIds) {
        orderRepository.deleteAllById(orderIds);
    }

    @Transactional
    public Order addOrder(Order order, int[] cartIds) {
        orderRepository.save(order);

        for (int cartId : cartIds){
            CartDetails cartDetails = cartDetailsRepository.findById(cartId).orElse(null);
            if (cartDetails != null){
                OrderDetail orderDetail = new OrderDetail();
                if (cartDetails.getProductType().equals("DEVICE")){
                    Device device = cartDetails.getDevice();
                    device.setQuantity(device.getQuantity() - cartDetails.getQuantity());
                    deviceRepository.save(device);
                    orderDetail.setProductType(ProductType.DEVICE);
                    orderDetail.setSellPrice((long)device.getPrice());
                    orderDetail.setDevice(device);
                } else if (cartDetails.getProductType().equals("ACCESSORY")) {
                    Accessory accessory = cartDetails.getAccessory();
                    accessory.setQuantity(accessory.getQuantity() - cartDetails.getQuantity());
                    accessoryRepository.save(accessory);
                    orderDetail.setProductType(ProductType.ACCESSORY);
                    orderDetail.setAccessory(accessory);
                    orderDetail.setSellPrice((long)accessory.getPrice());

                }
                orderDetail.setQuantity(cartDetails.getQuantity());
                orderDetail.setOrder(order);
                orderDetailRepository.save(orderDetail);
            }
            cartDetailsRepository.delete(cartDetails);
        }
        return orderRepository.save(order);
    }

    @Override
    public void updateOrder(Order order) {
        orderRepository.save(order);
    }
    @Override
    public Order addOrderPending(Order order, int[] cartIds) {
        orderRepository.save(order);

        for (int cartId : cartIds){
            CartDetails cartDetails = cartDetailsRepository.findById(cartId).orElse(null);
            if (cartDetails != null){
                OrderDetail orderDetail = new OrderDetail();
                if (cartDetails.getProductType().equals("DEVICE")){
                    Device device = cartDetails.getDevice();

                    orderDetail.setProductType(ProductType.DEVICE);
                    orderDetail.setSellPrice((long)device.getPrice());
                    orderDetail.setDevice(device);
                } else if (cartDetails.getProductType().equals("ACCESSORY")) {
                    Accessory accessory = cartDetails.getAccessory();

                    orderDetail.setProductType(ProductType.ACCESSORY);
                    orderDetail.setAccessory(accessory);
                    orderDetail.setSellPrice((long)accessory.getPrice());

                }
                orderDetail.setQuantity(cartDetails.getQuantity());
                orderDetail.setOrder(order);
                orderDetailRepository.save(orderDetail);
            }
            cartDetailsRepository.deleteCartDetailsById(cartId);
        }
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getOrderByUserId(int id) {
        return orderRepository.findOrdersByUserId(id);
    }

}

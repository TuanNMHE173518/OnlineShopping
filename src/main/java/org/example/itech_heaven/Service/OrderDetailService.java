package org.example.itech_heaven.Service;

import org.example.itech_heaven.Entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> getOrderDetails(int id);
}

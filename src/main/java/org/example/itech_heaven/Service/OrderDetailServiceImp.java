package org.example.itech_heaven.Service;

import org.example.itech_heaven.Entity.OrderDetail;
import org.example.itech_heaven.Repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImp implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public List<OrderDetail> getOrderDetails(int id) {
        return orderDetailRepository.findByOrderId(id);
    }

}

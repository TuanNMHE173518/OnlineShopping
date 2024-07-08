package org.example.itech_heaven.Controller.Profile;


import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.Order;
import org.example.itech_heaven.Entity.OrderDetail;
import org.example.itech_heaven.Entity.User;
import org.example.itech_heaven.Service.OrderDetailService;
import org.example.itech_heaven.Service.OrderService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class SelfOrderController {

    private final OrderService  orderService;
    private final OrderDetailService orderDetailService;

    @GetMapping("/order")
    public String getPageOrder(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        List<Order> listOrder = orderService.getOrderByUserId(user.getId());
        model.addAttribute("listOrder",listOrder);


        return "self-order";
    }

    @GetMapping("/order-detail/{id}")
    public String getPageOrderDetails(Model model, @PathVariable("id")int id){
        Order order = orderService.getOrderById(id);
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetails(id);
        model.addAttribute("listDetail", orderDetails);
        model.addAttribute("order", order);
        return "self-orderdetail";
    }
}

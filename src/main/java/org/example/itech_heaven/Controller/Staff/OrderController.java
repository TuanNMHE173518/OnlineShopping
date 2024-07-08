package org.example.itech_heaven.Controller.Staff;

import org.example.itech_heaven.Entity.Order;
import org.example.itech_heaven.Service.OrderDetailService;
import org.example.itech_heaven.Service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/manage-order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/listOrder")
    public String listOrder(Model model) {
        model.addAttribute("listOrder",orderService.getOrders());
        return "manage-order";
    }

    @PostMapping("/search")
    public String searchOrder(@RequestParam("name") String name,
                              @RequestParam("date") String date,
                              Model model) {

        if (!name.isEmpty() && !date.isEmpty()) {
            model.addAttribute("listOrder",orderService.getOrdersByDateAndName(name,LocalDate.parse(date)));
        } else if (!name.isEmpty()) {
            model.addAttribute("listOrder",orderService.getOrdersByUserName(name));
        } else if (!date.isEmpty()) {
            model.addAttribute("listOrder",orderService.getOrdersByDate(LocalDate.parse(date)));
        }
        else {
            model.addAttribute("listOrder",orderService.getOrders());
        }
        model.addAttribute("date",date);
        model.addAttribute("name",name);
        return "manage-order";
    }

    @GetMapping("/detail/{orderId}")
    public String detailOrder(@PathVariable("orderId") int orderId, Model model) {
        model.addAttribute("order",orderService.getOrderById(orderId));
        model.addAttribute("listDetail",orderDetailService.getOrderDetails(orderId));
        return "order-details";
    }

    @GetMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable("orderId") int orderId) {
        orderService.deleteOrder(orderId);
        return "redirect:/manage-order/listOrder";
    }

    @PostMapping("/deleteOrders")
    public String deleteOrders(@RequestParam(value = "orderId", defaultValue = "") List<Integer> orderId) {
        orderService.deleteOrdersById(orderId);
        return "redirect:/manage-order/listOrder";
    }

    @PostMapping("/changeStatus")
    public String changeStatus(@RequestParam("orderId") int orderId,
                              @RequestParam("status")  String status) {
        Order order = orderService.getOrderById(orderId);
        order.setStatus(status);
        orderService.updateOrder(order);
        return "redirect:/manage-order/detail/" + orderId;
    }
}

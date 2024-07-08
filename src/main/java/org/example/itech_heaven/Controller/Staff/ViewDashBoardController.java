package org.example.itech_heaven.Controller.Staff;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.Accessory;
import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.Feedback;
import org.example.itech_heaven.Entity.Order;
import org.example.itech_heaven.Service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/dashboard")
public class ViewDashBoardController {

    private final UserService userService;

    private final AccessoryService accessoryService;

    private final DeviceService deviceService;

    private final OrderService orderService;

    private final FeedbackService feedbackService;
    @GetMapping("")
    public String viewDashBoard(Model model) {

        long totalAccessories =accessoryService.getTotalAccessories();

        long totalDevices =deviceService.getTotalDevices();

        long totalProducts = totalDevices + totalAccessories;

        model.addAttribute("totalProductsToRestock",calculateTotalProductsRestock());

        model.addAttribute("totalUsers", userService.countUsers());

        model.addAttribute("newUsers",userService.findNewUsers());

        model.addAttribute("totalProducts",totalProducts);

        model.addAttribute("totalOrders", orderService.countOrders());

        List<Order> getAllOrders = orderService.getAllOrders();
        model.addAttribute("OrderList",getAllOrders);

        List<Feedback> recentFeedbacks =feedbackService.getRecentFeedbacks(20);
        model.addAttribute("recentFeedbacks",recentFeedbacks);

        return "indexSA";
    }



    private long calculateTotalProductsRestock(){
        List<Accessory> accessoriesToRestock =accessoryService.findAccessoriesToRestock();

        List<Device> devicesToRestock = deviceService.findDevicesToRestock();

        long totalAccessoriesToRestock =accessoriesToRestock.size();

        long totalDevicesToRestock = devicesToRestock.size();
        return totalDevicesToRestock + totalAccessoriesToRestock;
    }

}

package org.example.itech_heaven.Controller.Feedback;

import org.example.itech_heaven.Entity.Accessory;
import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.Feedback;
import org.example.itech_heaven.Entity.User;
import org.example.itech_heaven.Service.AccessoryService;
import org.example.itech_heaven.Service.DeviceService;
import org.example.itech_heaven.Service.FeedbackService;
import org.example.itech_heaven.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private UserService userService;
    @Autowired
    AccessoryService accessoryService;

    @PostMapping("/saveFeedback")
    public String saveFeedback(@RequestParam String content,
                               @RequestParam int star,
                               @RequestParam int feedbackId,
                               @RequestParam int userId,
                               @RequestParam int productId,
                               @RequestParam String typeProduct) {
        User user = userService.findById(userId);
        Feedback feedback = new Feedback();
        if(feedbackId != 0) {
            feedback = feedbackService.getFeedbackById(feedbackId);
        }

        feedback.setContent(content);
        feedback.setStar(star);
        feedback.setDate(LocalDate.now());
        feedback.setUser(user);
        if(typeProduct.equalsIgnoreCase("device")) {
            Device device = deviceService.getDeviceById(productId);
            feedback.setDevice(device);
            feedbackService.saveFeedback(feedback);
            return "redirect:/deviceDetail/" + productId;
        }
        else{
            Accessory accessory = accessoryService.getAccessoryById(productId);
            feedback.setAccessory(accessory);
            feedbackService.saveFeedback(feedback);
            return "redirect:/accessoryDetail/" + productId;
        }

    }

    @GetMapping("/deleteFeedback/{feedbackId}/{productId}/{typeProduct}")
    public String deleteFeedback(@PathVariable int feedbackId,
                                 @PathVariable int productId,
                                 @PathVariable String typeProduct) {
        feedbackService.deleteFeedback(feedbackId);
        if(typeProduct.equalsIgnoreCase("device")) {
            return "redirect:/deviceDetail/" + productId;
        }
        else {
            return "redirect:/accessoryDetail/" + productId;
        }
    }
}

package org.example.itech_heaven.Controller.Authentication;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.example.itech_heaven.Entity.TypeDevice;
import org.example.itech_heaven.Entity.User;
import org.example.itech_heaven.Service.DeviceTypeSerivce;
import org.example.itech_heaven.Service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class AuthenticationController {
    private final UserService userService;
    private final DeviceTypeSerivce deviceTypeSerivce;


    @GetMapping("/login")
    public String getFormLogin(Model model) {
        List<TypeDevice> devices = deviceTypeSerivce.getAllTypeDevices();
        model.addAttribute("devices", devices);

        return "login";

    }

    @GetMapping("/register")
    public String getFormRegister(Model model) {
        List<TypeDevice> devices = deviceTypeSerivce.getAllTypeDevices();
        model.addAttribute("devices", devices);
        User user = new User();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register")
    public String addUser(@Valid @ModelAttribute("user") User newuser, BindingResult result) {

        if (result.hasErrors()) {
            return "/register";
        }

        User user = userService.findByUsernameOrEmail(newuser.getUsername(), newuser.getEmail());
        if (user == null) {
            userService.addUser(newuser);
            return "redirect:/login?register";
        }
        return "redirect:/register?fail";
    }
}





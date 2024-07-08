package org.example.itech_heaven.Controller.LayoutController;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.TypeDevice;
import org.example.itech_heaven.Service.DeviceTypeSerivce;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/header")
@RequiredArgsConstructor
public class LayoutHeaderController {
    private final DeviceTypeSerivce deviceTypeSerivce;

    @GetMapping
    public String header(Model model) {
        List<TypeDevice> devices = deviceTypeSerivce.getAllTypeDevices();
        model.addAttribute("devices", devices);
        return "header-layout";
    }
}

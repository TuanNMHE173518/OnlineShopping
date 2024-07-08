package org.example.itech_heaven.Controller.Home;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.DTO.response.CartDetailResponse;
import org.example.itech_heaven.Entity.*;
import org.example.itech_heaven.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@RequestMapping("/home")
public class HomeController {

    private final DeviceTypeSerivce deviceTypeSerivce;
    private final DeviceService deviceService;
    private final CartService cartService;
    private final AccessoryService accessoryService;
    private final AccessoryCategoryService accessoryCategoryService;
    @GetMapping()
    public ModelAndView index(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User user = (User) principal;
                Integer userId = user.getId();
                Collection<CartDetailResponse> cartDetails = cartService.findCartDetailsByUserId(userId);
                model.addAttribute("carts", cartDetails);
                List<Device> deviceList = deviceService.getDevices();
                List<Accessory> accessoryList =accessoryService.getAllAccessory();
                setDefaultAllProductHomePage(model, deviceList, accessoryList);
                Set<Integer> uniqueProductOptionIds = new HashSet<>();
                for (CartDetailResponse item : cartDetails) {
                    uniqueProductOptionIds.add(item.getId());
                }
                int cartItemCountByProductOptionId = uniqueProductOptionIds.size();
                model.addAttribute("cartCount", cartItemCountByProductOptionId);
                setDefaultAllProductHomePage(model, deviceList, accessoryList);
                return new ModelAndView("index-2");
            }
        }
        List<Device> deviceList = deviceService.getDevices();
        List<Accessory> accessoryList =  accessoryService.getAllAccessory();
        setDefaultAllProductHomePage(model,deviceList,accessoryList);
        return new ModelAndView("index-2");
    }

    @GetMapping("/search/{typeDeviceId}")
    public String SearchDeviceByTypeDeviceId(Model model, @PathVariable int typeDeviceId) {
        List<Device> deviceList = deviceService.getDevicesByType(typeDeviceId);
        seDefaultDeviceHomePage(model, deviceList);
        return "index-2";
    }
    @GetMapping("/devices/{deviceName}")
    public String getProductByName(@PathVariable String deviceName, Model model) {
        List<Device> device =deviceService.getDevicesByName(deviceName);
        seDefaultDeviceHomePage(model, device);
        model.addAttribute("device", device);
       return "header-layout";
    }

    private void setDefaultAllProductHomePage(Model model, List<Device> deviceList, List<Accessory> accessoryList) {
        List<TypeDevice> typeDevices  = deviceTypeSerivce.getAllTypeDevices();
        List<AccessoryCategory> accessoryCategories  =  accessoryCategoryService.getAllAccessoryCategories();
        Map<TypeDevice, List<Device>> devicesByType = new HashMap<>();
        for (TypeDevice typeDevice : typeDevices ) {
            List<Device> devicesOfType = deviceList.stream()
                    .filter(device -> device.getTypeDevice().getId() == typeDevice.getId())
                    .limit(4)
                    .toList();
            devicesByType.put(typeDevice, devicesOfType);
        }
        model.addAttribute("typeDevices", typeDevices);
        model.addAttribute("devicesByType", devicesByType);
        model.addAttribute("accessoryCategory", accessoryCategories);
        model.addAttribute("accessoryList",accessoryList);
    }

    private void seDefaultDeviceHomePage(Model model, List<Device> deviceList) {
        List<TypeDevice> devices = deviceTypeSerivce.getAllTypeDevices();

        model.addAttribute("typeDevices", devices);
        model.addAttribute("deviceList", deviceList);
    }
}



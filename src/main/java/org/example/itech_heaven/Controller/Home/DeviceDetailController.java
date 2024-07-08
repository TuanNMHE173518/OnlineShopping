package org.example.itech_heaven.Controller.Home;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.itech_heaven.DTO.CartDTO;
import org.example.itech_heaven.DTO.request.CartDetailsRequest;
import org.example.itech_heaven.DTO.response.CartDetailResponse;
import org.example.itech_heaven.Entity.Cart;
import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.User;
import org.example.itech_heaven.Service.CartService;
import org.example.itech_heaven.Service.DeviceService;
import org.example.itech_heaven.Service.UserService;

import org.example.itech_heaven.Entity.Feedback;
import org.example.itech_heaven.Service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import java.util.Collection;
import java.util.HashSet;

import java.util.List;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/deviceDetail")
@AllArgsConstructor
public class DeviceDetailController {

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private CartService cartService;
    private final UserService userService;
    @Autowired
    private TypeDeviceService typeDeviceService;

    @GetMapping("/{deviceId}")
    public ModelAndView deviceDetails(Model model, @PathVariable int deviceId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // Lấy thông tin chi tiết của device
            Device device = deviceService.getDeviceById(deviceId);

            // Chuẩn bị Request cho giỏ hàng
            CartDetailsRequest cartDetailsRequest = new CartDetailsRequest();
            cartDetailsRequest.setDeviceId(deviceId);


            // Thêm thông tin device vào model
            model.addAttribute("device", device);
            model.addAttribute("listRom", deviceService.getRomByDeviceName(device.getName()));
            model.addAttribute("listColor", deviceService.getColorsOfDeviceByName(device.getName(), device.getROM()));

            // Nếu người dùng đã đăng nhập
            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof User) {
                    User user = (User) principal;
                    Integer userId = user.getId();
                    Collection<CartDetailResponse> cartDetails = cartService.findCartDetailsByUserId(userId);
                    model.addAttribute("carts", cartDetails);
                    model.addAttribute("userId", userId);
                    Set<Integer> uniqueProductOptionIds = new HashSet<>();
                    for (CartDetailResponse item : cartDetails) {
                        uniqueProductOptionIds.add(item.getId());
                    }
                    int cartItemCountByProductOptionId = uniqueProductOptionIds.size();
                    model.addAttribute("cartCount", cartItemCountByProductOptionId);
                }
            }

            // Thêm cartDetailsRequest vào model
            model.addAttribute("cartDetailsRequest", cartDetailsRequest);

            return new ModelAndView("product-details");
        } catch (Exception e) {
            // Xử lý lỗi nếu có
            model.addAttribute("errorMessage", "Lỗi khi hiển thị chi tiết sản phẩm.");
            return new ModelAndView("product-details");
        }
    }



    @PostMapping("/add-to-cart")
    public String addToCartForDevice(@Valid @ModelAttribute("cartDetailsRequest") CartDetailsRequest cartDetailsRequest,
                                     BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Device device = deviceService.getDeviceById(cartDetailsRequest.getDeviceId());
            model.addAttribute("device", device);
            model.addAttribute("cartDetailsRequest", cartDetailsRequest);
            return "product-details";
        }
        try {
            cartDetailsRequest.setProductType("DEVICE");
            cartService.addToCart(cartDetailsRequest);
            model.addAttribute("message", "Thêm vào giỏ hàng thành công!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            Device device = deviceService.getDeviceById(cartDetailsRequest.getDeviceId());
            model.addAttribute("device", device);
            model.addAttribute("request", cartDetailsRequest);
            return "product-details";
        }

        return "redirect:/deviceDetail/" + cartDetailsRequest.getDeviceId() +"?success=true";
    }
}

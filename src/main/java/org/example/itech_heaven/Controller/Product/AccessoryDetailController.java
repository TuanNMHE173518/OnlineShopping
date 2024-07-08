package org.example.itech_heaven.Controller.Product;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.itech_heaven.DTO.CartDTO;
import org.example.itech_heaven.DTO.request.CartDetailsRequest;
import org.example.itech_heaven.DTO.response.CartDetailResponse;
import org.example.itech_heaven.Entity.Accessory;
import org.example.itech_heaven.Entity.Cart;
import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.User;
import org.example.itech_heaven.Service.AccessoryService;
import org.example.itech_heaven.Service.CartService;
import org.example.itech_heaven.Service.TypeDeviceService;
import org.example.itech_heaven.Service.UserService;
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
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/accessoryDetail")
@AllArgsConstructor
public class AccessoryDetailController {
    @Autowired
    private AccessoryService accessoryService;
    @Autowired
    private CartService cartService;
    private final UserService userService;
    @Autowired
    private TypeDeviceService typeDeviceService;
    @GetMapping("/{accessoryId}")
    public ModelAndView accessoryDetails(Model model, @PathVariable int accessoryId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Lấy thông tin chi tiết của accessory
            Accessory accessory = accessoryService.getAccessoryById(accessoryId);

            // Chuẩn bị Request cho giỏ hàng
            CartDetailsRequest cartDetailsRequest = new CartDetailsRequest();
            cartDetailsRequest.setAccessoryId(accessoryId);
            cartDetailsRequest.setProductType("ACCESSORY"); // Đảm bảo thiết lập productType

            // Thêm thông tin accessory vào model
            model.addAttribute("accessory", accessory);
            model.addAttribute("typeDevices", typeDeviceService.getAllTypeDevice());
            model.addAttribute("listColor", accessoryService.getColorsOfAccessoryName(accessory.getName()));

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

            return new ModelAndView("accessory-details");
        } catch (Exception e) {
            // Xử lý lỗi nếu có
            model.addAttribute("errorMessage", "Lỗi khi hiển thị chi tiết sản phẩm.");
            return new ModelAndView("accessory-details");
        }
    }


    @PostMapping("/add-to-cart")
    public String addToCartForAccessory(@Valid @ModelAttribute("cartDetailsRequest") CartDetailsRequest cartDetailsRequest,
                                        BindingResult bindingResult,
                                        Model model) {
        if (bindingResult.hasErrors()) {
                Accessory accessory = accessoryService.getAccessoryById(cartDetailsRequest.getAccessoryId());
                model.addAttribute("accessory", accessory);
                model.addAttribute("cartDetailsRequest", cartDetailsRequest);
                return "accessory-details";
        }
        try {
            cartDetailsRequest.setProductType("ACCESSORY");
            cartService.addToCart(cartDetailsRequest);
            model.addAttribute("message", "Thêm vào giỏ hàng thành công!");

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            Accessory accessory = accessoryService.getAccessoryById(cartDetailsRequest.getAccessoryId());
            model.addAttribute("accessory", accessory);
            model.addAttribute("cartDetailsRequest", cartDetailsRequest);
            return "accessory-details";
        }
        return "redirect:/accessoryDetail/" + cartDetailsRequest.getAccessoryId();
    }

    @GetMapping("/{name}/{color}")
    public String selectRom(Model model, @PathVariable String name, @PathVariable String color) {
        Accessory accessory = accessoryService.getByNameAndColor(name,color);
        return "redirect:/accessoryDetail/" + accessory.getId();
    }
}

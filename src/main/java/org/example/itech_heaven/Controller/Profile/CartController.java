package org.example.itech_heaven.Controller.Profile;

import org.example.itech_heaven.DTO.RemoveItemCart;
import org.example.itech_heaven.DTO.response.CartDetailResponse;
import org.example.itech_heaven.Entity.Cart;
import org.example.itech_heaven.Entity.CartDetails;
import org.example.itech_heaven.Entity.User;
import org.example.itech_heaven.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/cart")
    public ModelAndView cartView(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User user = (User) principal;
                Integer userId = user.getId();
                Collection<CartDetailResponse> cartDetails = cartService.findCartDetailsByUserId(userId);
                model.addAttribute("carts", cartDetails);

                Set<Integer> uniqueProductOptionIds = new HashSet<>();
                for (CartDetailResponse item : cartDetails) {
                    uniqueProductOptionIds.add(item.getId());
                }
                int cartItemCountByProductOptionId = uniqueProductOptionIds.size();
                model.addAttribute("cartCount", cartItemCountByProductOptionId);
                return new ModelAndView("/cart");
            }
        }
        return new ModelAndView("redirect:/login");
    }

    @PostMapping("/cart/remove/{id}")
    public String removeItemFromCart(@PathVariable("id") int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User user = (User) principal;
                try {
                    cartService.deleteCartDetailsById(id);
                    return "redirect:/cart";
                } catch (RuntimeException e) {
                    // Xử lý ngoại lệ nếu cần thiết
                    throw new RuntimeException("Failed to remove item from cart: " + e.getMessage());
                }
            }
        }
        return "redirect:/login";
    }

}

package org.example.itech_heaven.Controller.Profile;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.User;
import org.example.itech_heaven.Service.EmailService;
import org.example.itech_heaven.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
@RequiredArgsConstructor
@RequestMapping("/profile")
public class
ProfileController {

    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String viewProfile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username);

        if (user != null) {
            model.addAttribute("user", user);

            return "profile";
        } else {
            return "redirect:/login?notfound";
        }
    }
    @GetMapping("/edit")
    public String showEditProfileForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userService.findByUsernameOrEmail(currentUsername, currentUsername);
        model.addAttribute("user", user);
        return "edit-profile";
    }

    @PostMapping("/edit")
    public String updateProfile(
            @Valid @ModelAttribute("user") User user,
            BindingResult bindingResult,
            @RequestParam("tinh") String tinh,
            @RequestParam("phuong") String phuong,
            @RequestParam("quan") String quan) {

        if (bindingResult.hasErrors()) {
            return "edit-profile";
        }

        if (tinh.equals("0") || quan.equals("0") || phuong.equals("0")) {
            bindingResult.rejectValue("address", "error.user", "Tỉnh, Quận, và Phường không được để trống");
            return "edit-profile";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.findByUsername(currentUsername);
        String fullAddress = user.getAddress() + " ," + phuong + " ," + quan + " ," + tinh;

        if (currentUser != null) {
            currentUser.setFirstname(user.getFirstname());
            currentUser.setLastname(user.getLastname());
            currentUser.setGender(user.isGender());
            currentUser.setEmail(user.getEmail());
            currentUser.setPhone(user.getPhone());
            currentUser.setAddress(fullAddress);

            userService.updateUser(currentUser);

            return "redirect:/profile";
        } else {
            return "redirect:/login?notfound";
        }
    }

    @GetMapping("/changePassword")
    public String showChangePasswordForm(Model model) {
        return "change-password";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session, Model model) {

        if (newPassword.length() < 6) {
            model.addAttribute("error", "Mật khẩu mới phải có từ 6 đến 20 kí tự.");
            return "change-password";
        }
        if (!newPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).+$")) {
            model.addAttribute("error", "Mật khẩu mới phải chứa ít nhất một chữ thường, một chữ hoa, một chữ số và một ký tự đặc biệt.");
            return "change-password";
        }

        if (newPassword.equals(oldPassword)) {
            model.addAttribute("error", "Mật khẩu mới không được trùng với mật khẩu cũ.");
            return "change-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Xác nhận mật khẩu không khớp.");
            return "change-password";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userService.findByUsername(currentUsername);

        if (currentUser != null) {
            if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
                model.addAttribute("error", "Mật khẩu cũ không chính xác.");
                return "change-password";
            }

            String verificationCode = String.valueOf(new Random().nextInt(999999));
            session.setAttribute("verificationCode", verificationCode);
            session.setAttribute("newPassword", newPassword);

            emailService.sendSimpleMessage(currentUser.getEmail(), "Mã xác nhận thay đổi mật khẩu",
                    "Mã xác nhận của bạn là : " + verificationCode);

            return "redirect:/profile/verifyCode";
        } else {
            return "redirect:/login?notfound";
        }
    }

    @GetMapping("/verifyCode")
    public String showVerifyCodeForm() {
        return "verify-code";
    }

    @PostMapping("/verifyCode")
    public String verifyCode(@RequestParam("verificationCode") String verificationCode,
                             HttpSession session, Model model) {
        String sessionVerificationCode = (String) session.getAttribute("verificationCode");
        String newPassword = (String) session.getAttribute("newPassword");

        if (verificationCode.equals(sessionVerificationCode)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User currentUser = userService.findByUsername(currentUsername);

            if (currentUser != null) {
                currentUser.setPassword(passwordEncoder.encode(newPassword));
                userService.updateUser(currentUser);
                session.removeAttribute("verificationCode");
                session.removeAttribute("newPassword");
                model.addAttribute("successMessage", "Thay đổi mật khẩu thành công!");
                return "verify-code";
            } else {
                return "redirect:/login?notfound";
            }
        } else {
            model.addAttribute("errorMessage", "Mã xác thực không hợp lệ!");
            return "verify-code";
        }
    }

}
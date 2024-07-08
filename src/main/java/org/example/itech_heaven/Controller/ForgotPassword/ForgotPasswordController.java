package org.example.itech_heaven.Controller.ForgotPassword;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.PasswordResetToken;
import org.example.itech_heaven.Entity.TypeDevice;
import org.example.itech_heaven.Entity.User;
import org.example.itech_heaven.Service.DeviceTypeSerivce;
import org.example.itech_heaven.Service.EmailSenderService;
import org.example.itech_heaven.Service.PasswordResetTokenService;

import org.example.itech_heaven.Service.UserService;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final UserService userService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final EmailSenderService emailSenderService;
    private final DeviceTypeSerivce deviceTypeSerivce;

    @GetMapping("/forgot-password")
    public String GetFormForgotPassword(Model model) {
        List<TypeDevice> devices = deviceTypeSerivce.getAllTypeDevices();
        model.addAttribute("devices", devices);
        return "forgot-password";
    }

    @PostMapping("forgot-password")
    public String postFormForgotPassword(@RequestParam(name = "username") String username) throws MessagingException {
        User user = userService.findByUsername(username);
        if (user != null) {
            PasswordResetToken token = passwordResetTokenService.createPasswordResetToken(user);
            String appUrl = "http://localhost:8080";
            String subject = "Password Reset Request";
            String link = appUrl + "/reset-password?token=" + token.getToken();
            String buttonText = "Đổi mật khẩu";
            String buttonHtml = "<a href='" + link + "' style='background-color: #008CBA; border: none; color: white; padding: 5px 10px; text-align: center; text-decoration: none; display: inline-block; font-size: 16px; margin: 4px 2px; cursor: pointer; border-radius: 5px;'>" + buttonText + "</a>";

            String text = "<h2>Xin chào!</h2><h3>Bạn quên mật khẩu của mình ấn vào link bên dưới để đặt lại mật khẩu của bạn.</h3> <br/>" + buttonHtml;
            emailSenderService.sendEmail(user.getEmail(), subject, text);

            return "redirect:/forgot-password?success";
        }

        return "redirect:/forgot-password?fail";
    }




    @GetMapping("/reset-password")
    public String GetFormResetPassword(@RequestParam(name = "token")String token, Model model) {
        List<TypeDevice> devices = deviceTypeSerivce.getAllTypeDevices();
        model.addAttribute("devices", devices);
        if(!passwordResetTokenService.isValidPasswordResetToken(token)){
            return "redirect:/forgot-password?invalid";
        }
        model.addAttribute("token", token);
        return "reset-password";

    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam(name = "token")String token,
                                       @RequestParam(name = "confirm-password")String password) {
        try {
            if (!passwordResetTokenService.isValidPasswordResetToken(token)) {
                return "redirect:/forgot-password?invalid";
            }

            userService.resetUserPassword(password, token);


            passwordResetTokenService.deleteToken(passwordResetTokenService.findByToken(token));

            return "redirect:/login?reset";
        } catch (Exception e) {

            e.printStackTrace();
            return "redirect:/reset-password?error";
        }
    }
}

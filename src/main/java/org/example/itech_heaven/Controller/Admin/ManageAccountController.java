package org.example.itech_heaven.Controller.Admin;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.Role;
import org.example.itech_heaven.Entity.User;
import org.example.itech_heaven.Repository.RoleRepository;
import org.example.itech_heaven.Service.EmailSenderService;
import org.example.itech_heaven.Service.RoleService;
import org.example.itech_heaven.Service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequiredArgsConstructor()
@RequestMapping("/admin")
public class ManageAccountController {

    private final UserService userService;
    private final RoleService roleService;
    private final EmailSenderService emailSenderService;

    @GetMapping("/manage-account")
    public String account(Model model,
                          @RequestParam(name = "search_role", defaultValue = "0")int roleid) {
        List<User> users = new ArrayList<>();
        if (roleid == 0 ){
             users = userService.findAll();
        }else{
            users = userService.findAllByRoles(roleid);
        }
        List<Role> roles = roleService.getRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("users", users);
        model.addAttribute("search_role", roleid);
        return "manage-account";
    }

    @GetMapping("/manage-account/disable")
    public String accountDisable(@RequestParam(value = "userid", required = true) int id) {
        userService.UpdateStatusOfUser(id, false);

        return "redirect:/admin/manage-account";

    }

    @GetMapping("/manage-account/enable")
    public String accountEnable(@RequestParam(value = "userid", required = true) int id) {
        userService.UpdateStatusOfUser(id, true);

        return "redirect:/admin/manage-account";

    }

    @PostMapping("/manage-account/update")
    public String updateUserRole(@RequestParam("user-id")String id,
                                 @RequestParam("user-role")int[] role_id) {
        int userId = Integer.parseInt(id.substring(2));
        userService.updateUserRole(userId, role_id);
        return "redirect:/admin/manage-account";
    }

    @GetMapping("/manage-account/reset")
    public String resetUserPassword(@RequestParam("userid")int id,
                                    @RequestParam("pass")int pass) throws MessagingException {
        User user = userService.findById(id);
        userService.resetUserPassword(user, pass);
        String subject ="Đặt lại mật khẩu về mặc định!";
        String text = "<h3>ITech Heaven xin chào!</h3>" +
                "<h3>Chúng tôi đã đặt lại mật khẩu tài khoản của bạn. Mật khẩu mặc định sẽ là "+pass+", vui lòng không tiết lộ cho bất kì ai!</h3><br/>" +
                "<br/><h3>Trân trọng, iTech Heaven</h3>";
        emailSenderService.sendEmail(user.getEmail(), subject, text);
        return "redirect:/admin/manage-account";
    }

    @PostMapping("/manage-account/disable-all")
    public String disableListUser(@RequestParam(name = "checkbox-user")int[] userids,
                                 @RequestParam(name = "all", defaultValue = "0")String disable_all){
        if(disable_all != null && !disable_all.equals("0")){
            userService.UpdateStatusAll(false);
            return "redirect:/admin/manage-account";
        }else{
            for(int id : userids){
                userService.UpdateStatusOfUser(id, false);
            }
        }

        return "redirect:/admin/manage-account";

    }

    @PostMapping("/manage-account/enable-all")
    public String enableListUser(@RequestParam(name = "checkbox-user")int[] userids,
                                  @RequestParam(name = "all", defaultValue = "0")String enable_all,
                                 Model model){
        if(enable_all != null && !enable_all.equals("0")){
            userService.UpdateStatusAll(true);
            return "redirect:/admin/manage-account";
        }else{
            for(int id : userids){
                userService.UpdateStatusOfUser(id, true);
            }
        }

        return "redirect:/admin/manage-account";

    }
}

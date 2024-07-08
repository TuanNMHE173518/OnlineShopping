package org.example.itech_heaven.Controller.Admin;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.Permission;
import org.example.itech_heaven.Entity.Role;
import org.example.itech_heaven.Service.PermissionService;
import org.example.itech_heaven.Service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ManageRoleController {

    private final RoleService roleService;
    private final PermissionService permissionService;

    @GetMapping("/manage-role")
    public String getRolePage(Model model){
        List<Permission> permissions = permissionService.findAll();
        List<Role> roles = roleService.getRoles();
        model.addAttribute("permissions", permissions);
        model.addAttribute("roles", roles);
        return "manage-role";
    }

    @PostMapping("/manage-role/update")
    public String processUpdateRole(@RequestParam(name = "role-id")String id,
                                    @RequestParam(name = "role-name")String role_name,
                                    @RequestParam(name = "role-des")String role_des,
                                    @RequestParam(name = "role-permission")int[] permissions){
        int role_id = Integer.parseInt(id.substring(2));
        roleService.updateRole(role_id, permissions, role_name, role_des);
        return "redirect:/admin/manage-role";
    }

    @PostMapping("/manage-role/add")
    public String processAddRole(@RequestParam(name = "role-name-add")String role_name,
                                 @RequestParam(name = "role-des-add")String role_des,
                                 @RequestParam(name = "role-permission")int[] permissions){
        roleService.addRole(role_name, role_des, permissions);
        return "redirect:/admin/manage-role";
    }

    @GetMapping("/manage-role/delete")
    public String accountDelete(@RequestParam(value = "role-id", required = true) int id) {
        roleService.deleteRole(id);

        return "redirect:/admin/manage-role";

    }
}

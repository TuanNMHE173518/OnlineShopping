package org.example.itech_heaven.Service;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.Permission;
import org.example.itech_heaven.Entity.Role;
import org.example.itech_heaven.Repository.PermissionRepository;
import org.example.itech_heaven.Repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public void updateRole(int id, int[] permission_ids, String name, String des) {
        List<Permission> permissions = new ArrayList<>();
        for (int pid : permission_ids){
            Permission permission = permissionRepository.findById(pid).orElse(null);
            permissions.add(permission);
        }
        Role role = roleRepository.findById(id).orElse(null);
        role.setName("ROLE_"+name);
        role.setDescription(des);
        role.setPermissions(permissions);
        roleRepository.save(role);

    }

    @Override
    @Transactional
    public void addRole(String name, String des, int[] permission_ids) {
        List<Permission> permissions = new ArrayList<>();
        for (int pid : permission_ids){
            Permission permission = permissionRepository.findById(pid).orElse(null);
            permissions.add(permission);
        }
        Role role = new Role();
        role.setName("ROLE_"+name);
        role.setDescription(des);
        role.setPermissions(permissions);
        roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteRole(int id) {
        Role role = roleRepository.findById(id).orElse(null);
        role.getPermissions().clear();
        role.getUsers().clear();
        roleRepository.deleteUserRolesByRole(id);
        roleRepository.deleteRolesPermissionsById(id);
        roleRepository.delete(role);
    }
}

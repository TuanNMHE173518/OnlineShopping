package org.example.itech_heaven.Service;

import org.example.itech_heaven.Entity.Role;

import java.util.List;


public interface RoleService {
    List<Role> getRoles();

    void updateRole(int id, int[] permission_ids, String name, String des);

    void addRole(String name, String des, int[] permission_ids);
    void deleteRole(int id);
}

package org.example.itech_heaven.Repository;

import org.example.itech_heaven.Entity.Permission;
import org.example.itech_heaven.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String name);



    @Modifying
    @Query(value = "DELETE FROM role_permission WHERE role_permission.role_id = :id ", nativeQuery = true)
    void deleteRolesPermissionsById(@Param("id")int id);

    @Modifying
    @Query(value = "DELETE FROM user_role WHERE user_role.role_id = :id ", nativeQuery = true)
    void deleteUserRolesByRole(@Param("id")int id);
}

package org.example.itech_heaven.Repository;

import org.example.itech_heaven.Entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Permission findByUrl(String url);

}

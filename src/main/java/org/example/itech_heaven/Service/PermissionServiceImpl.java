package org.example.itech_heaven.Service;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.Permission;
import org.example.itech_heaven.Repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService{
    private final PermissionRepository permissionRepository;

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }
}

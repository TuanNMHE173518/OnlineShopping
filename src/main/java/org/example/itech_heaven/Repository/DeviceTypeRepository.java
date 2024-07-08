package org.example.itech_heaven.Repository;

import org.example.itech_heaven.Entity.TypeDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceTypeRepository extends JpaRepository<TypeDevice,Integer> {
}

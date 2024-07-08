package org.example.itech_heaven.Repository;

import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.DeviceImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DeviceImageRepository extends JpaRepository<DeviceImage, Integer> {
    List<DeviceImage> findDeviceImageByDevice_Id(int deviceId);
    void deleteAllByDevice_Id(int deviceId);
}

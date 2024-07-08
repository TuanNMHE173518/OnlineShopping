package org.example.itech_heaven.Service;

import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.DeviceImage;
import org.example.itech_heaven.Repository.DeviceImageRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface DeviceImageService {
    List<DeviceImage> getDeviceImagesByDeviceId(int deviceId);
    void deleteDeviceImageByDeviceId(int deviceId);
    void saveDeviceImage(DeviceImage deviceImage);
}

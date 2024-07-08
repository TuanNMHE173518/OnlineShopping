package org.example.itech_heaven.Service;

import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.DeviceImage;
import org.example.itech_heaven.Repository.DeviceImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceImageServiceImp  implements DeviceImageService {

    @Autowired
    private DeviceImageRepository deviceImageRepository;


    @Override
    public List<DeviceImage> getDeviceImagesByDeviceId(int deviceId) {
        return deviceImageRepository.findDeviceImageByDevice_Id(deviceId);
    }

    @Override
    @Transactional
    public void deleteDeviceImageByDeviceId(int deviceId) {
        deviceImageRepository.deleteAllByDevice_Id(deviceId);
    }


    @Override
    public void saveDeviceImage(DeviceImage deviceImage) {
        deviceImageRepository.save(deviceImage);
    }

}


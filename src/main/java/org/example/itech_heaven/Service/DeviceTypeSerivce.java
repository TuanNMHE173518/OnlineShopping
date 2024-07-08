package org.example.itech_heaven.Service;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.TypeDevice;
import org.example.itech_heaven.Repository.DeviceTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DeviceTypeSerivce {
    private final DeviceTypeRepository deviceTypeRepository;

    public List<TypeDevice> getAllTypeDevices() {
        return deviceTypeRepository.findAll();
    }
}

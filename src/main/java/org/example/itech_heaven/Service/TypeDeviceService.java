package org.example.itech_heaven.Service;

import org.example.itech_heaven.Entity.TypeDevice;

import java.util.List;

public interface TypeDeviceService {
    List<TypeDevice> getAllTypeDevice();
    List<TypeDevice> getTypeDeviceByListTypeId(List<Integer> listOfTypeId);
}

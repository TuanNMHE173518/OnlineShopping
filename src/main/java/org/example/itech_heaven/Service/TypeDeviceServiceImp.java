package org.example.itech_heaven.Service;

import org.example.itech_heaven.Entity.TypeDevice;
import org.example.itech_heaven.Repository.TypeDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeDeviceServiceImp implements TypeDeviceService{

    @Autowired
    private TypeDeviceRepository typeDeviceRepository;

    @Override
    public List<TypeDevice> getAllTypeDevice() {
        return typeDeviceRepository.findAll();
    }

    @Override
    public List<TypeDevice> getTypeDeviceByListTypeId(List<Integer> listOfTypeId) {
        return typeDeviceRepository.findAllById(listOfTypeId);
    }

}

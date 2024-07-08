package org.example.itech_heaven.Service;

import org.example.itech_heaven.DTO.DeviceSaleDTO;
import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.DeviceImage;
import org.example.itech_heaven.Entity.TypeDevice;
import org.example.itech_heaven.Repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeviceServiceImp implements DeviceService{

    @Autowired
    private DeviceRepository deviceRepository;


    @Override
    public List<Device> getDevices() {
        return deviceRepository.findAll();
    }

    @Override
    public List<Device> getDevicesByName(String name) {
        return deviceRepository.findDevicesByNameContains(name);
    }

    @Override
    public List<Device> getDevicesByType(int typeDeviceId) {
        return deviceRepository.findDevicesByTypeDeviceId(typeDeviceId);
    }

    @Override
    public List<Device> findDevicesByTypeAndName(int typeDeviceId, String name) {
        return deviceRepository.findDevicesByTypeDeviceIdAndNameContains(typeDeviceId, name);
    }

    @Override
    public void saveDevice(Device device) {
        deviceRepository.save(device);
    }

    @Override
    public void deleteDevice(int deviceId) {
        deviceRepository.deleteById(deviceId);
    }

    @Override
    public Device getDeviceById(int deviceId) {
        if(deviceRepository.findById(deviceId).isPresent()){
            return deviceRepository.findById(deviceId).get();
        }
        return null;
    }

    @Override
    public List<Device> fineDevicesBySale(int saleId) {
        return deviceRepository.findBySale_Id(saleId);
    }

    @Override
    public Page<DeviceSaleDTO> getSaleDevices(int pageNo, String search, int typeDevice, String sortdevice) {
        Pageable pageable = PageRequest.of(pageNo, 4);
        if (sortdevice.equals("asc")) {
            return deviceRepository.findAllDevicesWithSaleAsc(pageable, search, typeDevice);
        } else {
            return deviceRepository.findAllDevicesWithSaleDesc(pageable, search, typeDevice);
        }
    }

    @Override
    public Page<Device> getAllDevicesByTypeDeviceId(Integer typeDeviceId, Pageable pageable) {
        return deviceRepository.findDevicesByTypeDeviceId(typeDeviceId, pageable);
    }

    @Override
    public List<String> getRomByDeviceName(String name) {
        List<Device> devices = deviceRepository.findDevicesByName(name);
        Set<String> roms = new HashSet<>();
        for (Device device : devices) {
            roms.add(device.getROM());
        }
        return roms.stream().toList();
    }

    @Override
    public Device getDeviceByNameAndRom(String name, String rom) {
        return deviceRepository.findFirstByNameAndROM(name,rom);
    }

    @Override
    public Device getDeviceByNameAndColor(String name, String color) {
        return deviceRepository.findDeviceByNameAndColorName(name,color);
    }

    @Override
    public Device getDeviceByNameAndRomAndColor(String name, String rom, String color) {
        return deviceRepository.findDeviceByNameAndROMAndColorName(name, rom, color);
    }


    @Override
    public HashMap<String,String> getColorsOfDeviceByName(String name, String rom) {
        List<Device> devices = deviceRepository.findDevicesByNameAndROM(name, rom);
        HashMap<String,String> colors = new HashMap<>();
        for (Device device : devices) {
            colors.put(device.getColorName(),device.getColor());
        }
        return colors;
    }
    @Override
    public long getTotalDevices() {
        return deviceRepository.count();
    }

    @Override
    public List<Device> findDevicesToRestock() {
        return deviceRepository.findByQuantityLessThanEqual(5);
    }

}

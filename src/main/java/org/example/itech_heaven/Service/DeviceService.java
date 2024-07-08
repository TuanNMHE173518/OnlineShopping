package org.example.itech_heaven.Service;

import org.example.itech_heaven.DTO.DeviceSaleDTO;
import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.DeviceImage;
import org.example.itech_heaven.Entity.TypeDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;

public interface DeviceService {
    List<Device> getDevices();
    List<Device> getDevicesByName(String name);
    List<Device> getDevicesByType(int typeDeviceId);
    List<Device> findDevicesByTypeAndName(int typeDeviceId, String name);
    void saveDevice(Device device);
    void deleteDevice(int deviceId);
    Device getDeviceById(int deviceId);
    List<Device> fineDevicesBySale(int saleId);
    Page<DeviceSaleDTO> getSaleDevices(int PageNo, String search, int typede, String sortdevice);


    Page<Device> getAllDevicesByTypeDeviceId(Integer typeDeviceId, Pageable pageable);


    HashMap<String,String> getColorsOfDeviceByName(String name,String rom);
    List<String> getRomByDeviceName(String name);
    Device getDeviceByNameAndRom(String name,String rom);
    Device getDeviceByNameAndColor(String name,String color);
    Device getDeviceByNameAndRomAndColor(String name,String rom,String color);
    long getTotalDevices();
    List<Device> findDevicesToRestock();
}

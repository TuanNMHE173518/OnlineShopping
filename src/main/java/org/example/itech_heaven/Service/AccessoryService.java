package org.example.itech_heaven.Service;

import org.example.itech_heaven.DTO.AccessorySaleDTO;
import org.example.itech_heaven.Entity.Accessory;
import org.springframework.data.domain.Page;
import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.TypeDevice;
import org.example.itech_heaven.Repository.AccessoryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AccessoryService {
    List<Accessory> getAllAccessory();
    void SaveAccessory(Accessory accessory, List<Integer> typeDeviceId);
    void Save(Accessory accessory);
    void DeleteAccessoryById(int accessoryId);
    List<Accessory> getAccessoryByName(String accessoryName);
    List<Accessory> getAccessoryByTypeDeviceId(int typeDeviceId);
    List<Accessory> getAccessoryByType(int accessoryType);
    Accessory getAccessoryById(int accessoryId);
    List<Accessory> searchByAccessoryIdAndName(int accessoryCategoryId, String name);
    List<Accessory> searchByTypeDeviceAndName(int typeDevicesId, String name);
    List<Accessory> search(int accessoryCategoryId, int typeDevicesId, String name);
    List<Accessory> getBySaleId(int saleId);
    List<Accessory> getByName(String accessoryName);
    Accessory getByNameAndColor(String accessoryName, String color);
    HashMap<String,String> getColorsOfAccessoryName(String name);
    Page<AccessorySaleDTO> getSaleAccessory(int PageNo, String searchacc, int typeaccessory, String sortacc);
    Page<Accessory> getAllAccessory( Pageable pageable);
    long getTotalAccessories();
    List<Accessory> findAccessoriesToRestock();
}

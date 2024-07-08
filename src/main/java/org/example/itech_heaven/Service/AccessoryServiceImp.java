package org.example.itech_heaven.Service;

import org.example.itech_heaven.DTO.AccessorySaleDTO;
import org.example.itech_heaven.Entity.Accessory;
import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.TypeDevice;
import org.example.itech_heaven.Repository.AccessoryRepository;
import org.example.itech_heaven.Repository.TypeDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class AccessoryServiceImp implements AccessoryService {

    @Autowired
    private AccessoryRepository accessoryRepository;

    @Autowired
    private TypeDeviceRepository typeDeviceRepository;

    @Override
    public List<Accessory> getAllAccessory() {
        return accessoryRepository.findAll();
    }

    @Override
    public void SaveAccessory(Accessory accessory,List<Integer> typeDeviceId) {
        accessory.setTypeDevices(typeDeviceRepository.findAllById(typeDeviceId));
        accessory.setId(generateId());
        accessoryRepository.save(accessory);
    }

    private int generateId() {
        // Lấy thời gian hiện tại
        LocalDateTime currentTime = LocalDateTime.now();

        // Định dạng giờ phút giây mili giây
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HHmmssSSS"));

        // Chuyển đổi thành số nguyên
        return Integer.parseInt(formattedTime);
    }


    @Override
    public void Save(Accessory accessory) {
        accessoryRepository.save(accessory);
    }

    @Override
    public void DeleteAccessoryById(int accessoryId) {
        accessoryRepository.deleteById(accessoryId);
    }

    @Override
    public List<Accessory> getAccessoryByName(String accessoryName) {
        return accessoryRepository.findByNameContaining(accessoryName);
    }

    @Override
    public List<Accessory> getAccessoryByTypeDeviceId(int typeDeviceId) {
        return accessoryRepository.findByTypeDevices_Id(typeDeviceId);
    }

    @Override
    public List<Accessory> getAccessoryByType(int accessoryType) {
        return accessoryRepository.findByAccessoryCategory_Id(accessoryType);
    }

    @Override
    public Accessory getAccessoryById(int accessoryId) {
        if (accessoryRepository.findById(accessoryId).isPresent()) {
            return accessoryRepository.findById(accessoryId).get();
        }
        return null;
    }

    @Override
    public List<Accessory> searchByAccessoryIdAndName(int accessoryCategoryId, String name) {
        return accessoryRepository.findByAccessoryCategory_IdAndNameContaining(accessoryCategoryId,name);
    }

    @Override
    public List<Accessory> searchByTypeDeviceAndName(int typeDevicesId, String name) {
        return accessoryRepository.findByTypeDevices_IdAndNameContaining(typeDevicesId,name);
    }

    @Override
    public List<Accessory> search(int accessoryCategoryId, int typeDevicesId, String name) {
        return accessoryRepository.findByAccessoryCategory_IdAndTypeDevices_IdAndNameContaining(accessoryCategoryId,typeDevicesId,name);
    }

    @Override
    public List<Accessory> getBySaleId(int saleId) {
        return accessoryRepository.findBySale_Id(saleId);
    }

    @Override
    public Page<AccessorySaleDTO> getSaleAccessory(int pageNo, String searchacc, int typeAccessory,  String sortacc) {
         Pageable pageable = PageRequest.of(pageNo, 4);
        if (sortacc.equals("asc")) {
            return accessoryRepository.findByAccessoryWithSaleASC(pageable,searchacc, typeAccessory) ;
        } else {
            return accessoryRepository.findByAccessoryWithSaleDESC(pageable, searchacc, typeAccessory);
        }

    }


    @Override
    public Page<Accessory> getAllAccessory( Pageable pageable) {
        return accessoryRepository.findAll(pageable);
    }

    @Override
    public long getTotalAccessories() {
        return accessoryRepository.count();
    }

    @Override
    public List<Accessory> findAccessoriesToRestock() {
        return accessoryRepository.findByQuantityLessThanEqual(5);
    }

    @Override
    public List<Accessory> getByName(String accessoryName) {
        return accessoryRepository.findAccessoryByName(accessoryName);
    }

    @Override
    public Accessory getByNameAndColor(String accessoryName, String color) {
        return accessoryRepository.findAccessoryByNameAndColorName(accessoryName,color);
    }

    @Override
    public HashMap<String,String> getColorsOfAccessoryName(String name) {
        List<Accessory> accessories = accessoryRepository.findAccessoryByName(name);
        HashMap<String,String> colors = new HashMap<>();
        for (Accessory accessory : accessories) {
            colors.put(accessory.getColorName(),accessory.getColor());
        }
        return colors;
    }
}

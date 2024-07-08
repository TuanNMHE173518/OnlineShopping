package org.example.itech_heaven.Service;

import org.example.itech_heaven.DTO.DeviceSaleDTO;
import org.example.itech_heaven.Entity.AccessoryCategory;
import org.example.itech_heaven.Repository.AccessoryCategoryRepository;
import org.example.itech_heaven.Repository.AccessoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessoryCategoryServiceImp implements AccessoryCategoryService {
    @Autowired
    private AccessoryCategoryRepository accessoryCategoryRepository;

    @Override
    public List<AccessoryCategory> getAllAccessoryCategories() {
        return accessoryCategoryRepository.findAll();
    }

}

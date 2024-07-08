package org.example.itech_heaven.Service;

import org.example.itech_heaven.DTO.DeviceSaleDTO;
import org.example.itech_heaven.Entity.AccessoryCategory;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccessoryCategoryService {
    List<AccessoryCategory> getAllAccessoryCategories();

}

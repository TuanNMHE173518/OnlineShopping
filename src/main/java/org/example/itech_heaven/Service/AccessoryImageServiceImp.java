package org.example.itech_heaven.Service;

import org.example.itech_heaven.Repository.AccessoryImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccessoryImageServiceImp implements AccessoryImageService{

    @Autowired
    private AccessoryImageRepository accessoryImageRepository;


    @Override
    @Transactional
    public void deleteByAccessoryId(int accessoryId) {
        accessoryImageRepository.deleteAllByAccessory_Id(accessoryId);
    }
}

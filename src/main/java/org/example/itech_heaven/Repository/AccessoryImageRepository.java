package org.example.itech_heaven.Repository;

import org.example.itech_heaven.Entity.AccessoryImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessoryImageRepository extends JpaRepository<AccessoryImage, Integer> {
    void deleteAllByAccessory_Id(int accessoryId);
}

package org.example.itech_heaven.Repository;

import org.example.itech_heaven.DTO.AccessorySaleDTO;
import org.example.itech_heaven.Entity.Accessory;
import org.example.itech_heaven.Entity.Device;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccessoryRepository extends JpaRepository<Accessory, Integer> {
    List<Accessory> findByNameContaining(String accessoryName);
    List<Accessory> findByAccessoryCategory_Id(int accessoryCategoryId);
    List<Accessory> findByTypeDevices_Id(int typeDevicesId);
    List<Accessory> findByAccessoryCategory_IdAndNameContaining(int accessoryCategoryId, String name);
    List<Accessory> findByTypeDevices_IdAndNameContaining(int typeDevicesId, String name);
    List<Accessory> findByAccessoryCategory_IdAndTypeDevices_IdAndNameContaining(int accessoryCategoryId, int typeDevicesId, String name);
    List<Accessory> findBySale_Id(int saleId);
    List<Accessory> findByQuantityLessThanEqual(int quantity);

    @Query("select distinct name from Accessory ")
    List<String> findDistinctByName();

    @Query("SELECT new org.example.itech_heaven.DTO.AccessorySaleDTO(a.id, a.name, a.price, t.id, s.discount, s.id, a.mainImage,t.name) " +
            "FROM Accessory a " +
            "JOIN a.sale s " +
            "JOIN a.accessoryCategory t " +
            "WHERE a.name LIKE CONCAT('%', :searchacc, '%') " +
            "AND (:typeaccessory = 0 OR a.accessoryCategory.id  = :typeaccessory ) " +
            "AND CURRENT_DATE BETWEEN s.startDate AND s.endDate " +
            "AND a.id = (SELECT MIN(a2.id) FROM Accessory a2 WHERE a2.name = a.name) " +
            "ORDER BY (a.price * (1 - s.discount / 100.0)) ASC")
    Page<AccessorySaleDTO> findByAccessoryWithSaleASC(Pageable pageable, @Param("searchacc") String searchacc, @Param("typeaccessory") int typeaccessory );

    @Query("SELECT new org.example.itech_heaven.DTO.AccessorySaleDTO(a.id, a.name, a.price, t.id, s.discount, s.id, a.mainImage,t.name) " +
            "FROM Accessory a " +
            "JOIN a.sale s " +
            "JOIN a.accessoryCategory t " +
            "WHERE a.name LIKE CONCAT('%', :searchacc, '%') " +
            "AND (:typeaccessory  = 0 OR a.accessoryCategory.id = :typeaccessory) " +
            "AND CURRENT_DATE BETWEEN s.startDate AND s.endDate " +
            "AND a.id = (SELECT MIN(a2.id) FROM Accessory a2 WHERE a2.name = a.name) " +
            "ORDER BY (a.price * (1 - s.discount / 100.0)) DESC")
    Page<AccessorySaleDTO> findByAccessoryWithSaleDESC(Pageable pageable, @Param("searchacc") String searchacc, @Param("typeaccessory") int typeaccessory );
    List<Accessory> findAccessoryByName(String accessoryName);
    Accessory findAccessoryByNameAndColorName(String accessoryName, String color);
}

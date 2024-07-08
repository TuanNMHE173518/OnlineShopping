package org.example.itech_heaven.Repository;

import org.example.itech_heaven.DTO.DeviceSaleDTO;
import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.TypeDevice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Integer> {
    List<Device> findDevicesByTypeDeviceId(int typeDeviceId);
    List<Device> findDevicesByNameContains(String name);
    List<Device> findDevicesByTypeDeviceIdAndNameContains(int typeDeviceId, String name);
    List<Device> findBySale_Id(int saleId);
    Page<Device> findDevicesByTypeDeviceId(int typeDeviceId, Pageable pageable);

    Device findFirstByName(String name);
    List<Device> findDevicesByName(String name);
    List<Device> findDevicesByNameAndROM(String name,String ROM);
    Device findFirstByNameAndROM(String name,String ROM);
    Device findDeviceByNameAndColorName(String name,String color);
    Device findDeviceByNameAndROMAndColorName(String name,String rom,String color);
//    @Query("SELECT d FROM Device d WHERE d.sale IS NOT NULL")
//    List<Device> findAllDevicesWithSale() ;
    @Query("select distinct name from Device")
    List<String> findDistinctByName();

    @Query("SELECT new org.example.itech_heaven.DTO.DeviceSaleDTO(d.id, d.name, d.price, t.id, s.discount, s.id, d.mainImage,t.name) " +
            "FROM Device d " +
            "JOIN d.sale s " +
            "JOIN d.typeDevice t " +
            "WHERE d.name LIKE CONCAT('%', :search, '%') " +
            "AND (:typedevice = 0 OR d.typeDevice.id = :typedevice) " +
            "AND CURRENT_DATE BETWEEN s.startDate AND s.endDate " +
            "AND d.id = (SELECT MIN(d2.id) FROM Device d2 WHERE d2.name = d.name) " +
            "ORDER BY (d.price * (1 - s.discount)) ASC")
    Page<DeviceSaleDTO> findAllDevicesWithSaleAsc(Pageable pageable, @Param("search") String search, @Param("typedevice") int typedevice);
    @Query("SELECT new org.example.itech_heaven.DTO.DeviceSaleDTO(d.id, d.name, d.price, t.id, s.discount, s.id, d.mainImage,t.name) " +
            "FROM Device d " +
            "JOIN d.sale s " +
            "JOIN d.typeDevice t " +
            "WHERE d.name LIKE CONCAT('%', :search, '%') " +
            "AND (:typedevice = 0 OR d.typeDevice.id = :typedevice) " +
            "AND CURRENT_DATE BETWEEN s.startDate AND s.endDate " +
            "AND d.id = (SELECT MIN(d2.id) FROM Device d2 WHERE d2.name = d.name) " +
            "ORDER BY (d.price * (1 - s.discount)) desc ")
    Page<DeviceSaleDTO> findAllDevicesWithSaleDesc(Pageable pageable, @Param("search") String search, @Param("typedevice") int typedevice);


    @Query("SELECT d FROM Device d WHERE d.sale IS NOT NULL AND d.name LIKE CONCAT('%', :search, '%')")
    Page<Device> findAllDevicesWithSale(Pageable pageable, @Param(value = "search") String search);
    List<Device> findByQuantityLessThanEqual(int quantity);
}

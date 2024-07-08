package org.example.itech_heaven.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.itech_heaven.Entity.Sale;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceSaleDTO {
    private int deviceId;
    private String deviceName;
    private double devicePrice;
    private int deviceType;
    private int deviceDiscount;
    private int saleId;
    private String mainImage;
    private String typeDeviceName;





}

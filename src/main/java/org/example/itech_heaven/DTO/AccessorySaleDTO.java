package org.example.itech_heaven.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessorySaleDTO {
    private int accessoryId;
    private String accessoryName;
    private double accessoryPrice;
    private int accessoryCategory;
    private int accessoryDiscount;
    private int saleId;
    private String mainImage;
    private String accessoryCategoryName;


}

package org.example.itech_heaven.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailResponse {
    private Integer id;
    private Integer deviceId;
    private Integer accessoryId;
    private String productImage ;
    private String productName;
    private String productCategory;
    private String colorName;
    private Double productPrice;
    private Integer productQuantity ; // Số lượng trong device , accessory
    private Integer quantity; // Số lượng trong giỏ hàng
    private Double salePrice ;
    private Integer salePriceDiscount ;
    private Double totalPrice ;
    private Double totalPriceDiscount ;
    private String productType ;

    public String formatPrice(double pri){
        DecimalFormat decimalFormat = new DecimalFormat("#,###.###");
        return decimalFormat.format((int) pri) + " đ";
    }
}

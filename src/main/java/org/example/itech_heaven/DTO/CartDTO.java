package org.example.itech_heaven.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartDTO {

    private Long id;

    @NotNull(message = "Số lượng sản phẩm không được để trống")
    @Min(value = 1, message = "Số lượng sản phẩm phải lớn hơn 0")
    private Integer quantity;

    private int productId;

    private String productType;
}

package org.example.itech_heaven.DTO.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailsRequest {
    private Long id;

    @Min(value = 1, message = "Quantity must be greater or equals than 1")
    private Integer quantity;

    private Integer deviceId;

    private Integer accessoryId;

    private String productType;
}

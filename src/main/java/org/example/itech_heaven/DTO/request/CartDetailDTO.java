package org.example.itech_heaven.DTO.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailDTO {
    private int id;

    private int quantity;
}

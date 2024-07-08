package org.example.itech_heaven.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
    private Long id;
    private Integer totalProduct;
    private List<CartDetailResponse> cartDetails;
}

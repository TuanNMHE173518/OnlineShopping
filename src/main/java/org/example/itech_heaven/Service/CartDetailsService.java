package org.example.itech_heaven.Service;

import org.example.itech_heaven.DTO.request.CartDetailDTO;
import org.example.itech_heaven.Entity.CartDetails;

import java.util.List;

public interface CartDetailsService {
    void updateListCartDetail(CartDetailDTO[] cartDetailDTOs);

    CartDetails findById(int id);
}

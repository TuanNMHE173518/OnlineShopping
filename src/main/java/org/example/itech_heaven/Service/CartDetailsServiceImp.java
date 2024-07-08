package org.example.itech_heaven.Service;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.DTO.request.CartDetailDTO;
import org.example.itech_heaven.Entity.CartDetails;
import org.example.itech_heaven.Repository.CartDetailsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartDetailsServiceImp implements CartDetailsService{
    private final CartDetailsRepository cartDetailsRepository;

    @Override
    @Transactional
    public void updateListCartDetail(CartDetailDTO[] cartDetailDTOs) {
        for (CartDetailDTO cartdetail : cartDetailDTOs){
            CartDetails cartDetails = cartDetailsRepository.findById(cartdetail.getId()).orElse(null);
            cartDetails.setQuantity(cartdetail.getQuantity());
            cartDetailsRepository.save(cartDetails);
        }
    }

    @Override
    public CartDetails findById(int id) {
       return cartDetailsRepository.findById(id).orElse(null);
    }
}

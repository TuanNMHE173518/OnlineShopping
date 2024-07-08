package org.example.itech_heaven.Service;

import org.example.itech_heaven.DTO.CartDTO;
import org.example.itech_heaven.DTO.RemoveItemCart;
import org.example.itech_heaven.DTO.request.CartDetailsRequest;
import org.example.itech_heaven.DTO.response.CartDetailResponse;
import org.example.itech_heaven.Entity.Cart;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Service
public interface CartService {
    CartDetailResponse addToCart(CartDetailsRequest request) throws Exception;

    Collection<Cart> findAllByUserId(Integer userId);


    List<CartDetailResponse> findCartDetailsByUserId(Integer userId);

    void deleteCartDetailsById(Integer id);

    Map<String,String> checkCart(String email, int[] cartIds);
}

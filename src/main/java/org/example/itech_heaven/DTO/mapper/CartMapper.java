package org.example.itech_heaven.DTO.mapper;

import lombok.AllArgsConstructor;
import org.example.itech_heaven.DTO.request.CartDetailsRequest;
import org.example.itech_heaven.DTO.response.CartDetailResponse;
import org.example.itech_heaven.DTO.response.CartResponse;
import org.example.itech_heaven.Entity.*;
import org.example.itech_heaven.Repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class CartMapper {
    final ModelMapper modelMapper;
    final UserRepository userRepository;
    final CartRepository cartRepository;
    final SaleRepository saleRepository;
    final DeviceRepository deviceRepository;
    final AccessoryRepository accessoryRepository;

    public CartResponse convertToResponse(Cart cart) {
        CartResponse cartResponse = modelMapper.map(cart, CartResponse.class);
        cartResponse.setTotalProduct(cart.getCartDetails().size());

        // sắp xếp theo updatedAt cảu cartDetails
        cartResponse.setCartDetails(cart.getCartDetails().stream()
                .map(this::convertToResponse)
                .sorted((o1, o2) -> (int) (o2.getId() - o1.getId()))
                .collect(Collectors.toList()));
        return cartResponse;
    }

    public CartDetailResponse convertToResponse(CartDetails cartDetails) {
        CartDetailResponse cartDetailsResponse = modelMapper.map(cartDetails, CartDetailResponse.class);

        if ("DEVICE".equals(cartDetails.getProductType())) {
            Device device = cartDetails.getDevice();
            cartDetailsResponse.setDeviceId(device.getId());
            cartDetailsResponse.setProductName(device.getName());
            cartDetailsResponse.setColorName(device.getColorName());
            cartDetailsResponse.setProductImage(device.getMainImage());
            cartDetailsResponse.setProductPrice(device.getPrice());
            cartDetailsResponse.setProductQuantity(device.getQuantity());
            cartDetailsResponse.setProductCategory(cartDetails.getDevice().getTypeDevice().getName());
            cartDetailsResponse.setTotalPrice(device.getPrice() * cartDetails.getQuantity());
            cartDetailsResponse.setQuantity(cartDetails.getQuantity());

            // Xử lý Sale của Device nếu có
            if (device.getSale() != null) {
                cartDetailsResponse.setSalePriceDiscount(device.getSale().getDiscount());
                double discount = (double) device.getSale().getDiscount() / 100;
                double salePrice = device.getPrice() * (1 - discount);
                cartDetailsResponse.setSalePrice(salePrice);
                double totalPriceDiscount = salePrice * cartDetails.getQuantity();
                cartDetailsResponse.setTotalPriceDiscount(totalPriceDiscount);
            } else {
                // Nếu không có Sale, giá bán và tổng giá giảm giá sẽ bằng nhau
                cartDetailsResponse.setSalePrice(null);
                cartDetailsResponse.setSalePriceDiscount(0);
                cartDetailsResponse.setTotalPriceDiscount(cartDetailsResponse.getTotalPrice());
            }
        }

        if ("ACCESSORY".equals(cartDetails.getProductType())) {
            Accessory accessory = cartDetails.getAccessory();
            cartDetailsResponse.setAccessoryId(accessory.getId());
            cartDetailsResponse.setProductName(accessory.getName());
            cartDetailsResponse.setColorName(accessory.getColorName());
            cartDetailsResponse.setProductImage(accessory.getMainImage());
            cartDetailsResponse.setProductPrice(accessory.getPrice());
            cartDetailsResponse.setProductQuantity(accessory.getQuantity());
            cartDetailsResponse.setProductCategory(cartDetails.getAccessory().getAccessoryCategory().getName());
            cartDetailsResponse.setTotalPrice(accessory.getPrice() * cartDetails.getQuantity());
            cartDetailsResponse.setQuantity(cartDetails.getQuantity());

            // Xử lý Sale của Accessory nếu có
            Sale sale = accessory.getSale();
            if (sale != null) {
                cartDetailsResponse.setSalePriceDiscount(sale.getDiscount());
                double discount = (double) sale.getDiscount() / 100;
                double salePrice = accessory.getPrice() * (1 - discount);
                cartDetailsResponse.setSalePrice(salePrice);
                double totalPriceDiscount = salePrice * cartDetails.getQuantity();
                cartDetailsResponse.setTotalPriceDiscount(totalPriceDiscount);
            } else {
                // Nếu không có Sale, giá bán và tổng giá giảm giá sẽ bằng nhau
                cartDetailsResponse.setSalePrice(null);
                cartDetailsResponse.setSalePriceDiscount(0);
                cartDetailsResponse.setTotalPriceDiscount(cartDetailsResponse.getTotalPrice());
            }
        }

        return cartDetailsResponse;
    }


    public CartDetails convertToEntity(CartDetailsRequest cartDetailsRequest) {
        CartDetails cartDetails = modelMapper.map(cartDetailsRequest, CartDetails.class);
        cartDetails.setQuantity(cartDetailsRequest.getQuantity());
        cartDetails.setDevice(deviceRepository.findById(cartDetailsRequest.getDeviceId()).orElse(null));
        cartDetails.setAccessory(accessoryRepository.findById(cartDetailsRequest.getAccessoryId()).orElse(null));
        cartDetails.setCart(Objects.requireNonNull(userRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())).getCart());
        return cartDetails;
    }
}

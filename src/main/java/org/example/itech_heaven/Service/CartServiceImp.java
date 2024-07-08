package org.example.itech_heaven.Service;

import org.example.itech_heaven.DTO.CartDTO;
import org.example.itech_heaven.DTO.RemoveItemCart;
import org.example.itech_heaven.DTO.mapper.CartMapper;
import org.example.itech_heaven.DTO.request.CartDetailsRequest;
import org.example.itech_heaven.DTO.response.CartDetailResponse;
import org.example.itech_heaven.Entity.*;
import org.example.itech_heaven.Repository.AccessoryRepository;
import org.example.itech_heaven.Repository.CartDetailsRepository;
import org.example.itech_heaven.Repository.CartRepository;
import org.example.itech_heaven.Repository.DeviceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component

public class CartServiceImp implements CartService{

    @Autowired
    private DeviceRepository deviceRepository ;
    @Autowired
    private UserService userService ;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartDetailsRepository cartDetailsRepository;
    @Autowired
    private AccessoryRepository accessoryRepository;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDetailResponse addToCart(CartDetailsRequest request) throws Exception {
        Optional<String> currentUserEmailOpt = userService.getCurrentUserEmail();
        if (currentUserEmailOpt.isEmpty()) {
            throw new RuntimeException("User not authenticated");
        }
        String currentUserEmail = currentUserEmailOpt.get();

        Cart cart = cartRepository.findByUserEmail(currentUserEmail);

        // Kiểm tra sản phẩm đã có trong giỏ hàng hay chưa
        Optional<CartDetails> cartDetailsOptional = null;
        if ("DEVICE".equals(request.getProductType())) {
            cartDetailsOptional = cartDetailsRepository.findByCartIdAndDeviceId(cart.getId(), request.getDeviceId());
        } else if ("ACCESSORY".equals(request.getProductType())) {
            cartDetailsOptional = cartDetailsRepository.findByCartIdAndAccessoryId(cart.getId(), request.getAccessoryId());
        }

        if (cartDetailsOptional.isPresent()) {
            CartDetails cartDetails = cartDetailsOptional.get();
            // Nếu quá số lượng sản phẩm trong kho thì chỉ thêm số lượng sản phẩm còn lại
            if (cartDetails.getQuantity() + request.getQuantity() > cartDetails.getDevice().getQuantity()) {
                cartDetails.setQuantity(cartDetails.getDevice().getQuantity());
            } else {
                // Nếu sản phẩm đã có trong giỏ hàng thì cộng thêm số lượng sản phẩm
                cartDetails.setQuantity(cartDetails.getQuantity() + request.getQuantity());
            }
            return cartMapper.convertToResponse(cartDetailsRepository.save(cartDetails));
        } else {
            // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới vào
            CartDetails cartDetails = new CartDetails();
            cartDetails.setCart(cart);
            cartDetails.setQuantity(request.getQuantity());
            cartDetails.setProductType(request.getProductType());

            if ("DEVICE".equals(request.getProductType())) {
                Optional<Device> deviceOptional = deviceRepository.findById(request.getDeviceId());
                if (deviceOptional.isPresent()) {
                    Device device = deviceOptional.get();
                    cartDetails.setDevice(device);
                    cartDetails.setAccessory(null);
                } else {
                    throw new RuntimeException("Không tìm thấy sản phẩm DEVICE");
                }
            } else if ("ACCESSORY".equals(request.getProductType())) {
                Optional<Accessory> accessoryOptional = accessoryRepository.findById(request.getAccessoryId());
                if (accessoryOptional.isPresent()) {
                    Accessory accessory = accessoryOptional.get();
                    cartDetails.setAccessory(accessory);
                    cartDetails.setDevice(null);
                } else {
                    throw new RuntimeException("Không tìm thấy sản phẩm ACCESSORY");
                }
            }

            return cartMapper.convertToResponse(cartDetailsRepository.save(cartDetails));
        }
    }

    @Override
    public Collection<Cart> findAllByUserId(Integer userId) {
        return cartRepository.findAllByUserId(userId);
    }

    public List<CartDetailResponse> findCartDetailsByUserId(Integer userId) {
        List<CartDetailResponse> cartDetailResponses = new ArrayList<>();

        // Lấy danh sách cart details của user có userId
        List<CartDetails> cartDetailsList = cartDetailsRepository.findByCartUserId(userId);

        for (CartDetails cartDetails : cartDetailsList) {
            CartDetailResponse cartDetailResponse = modelMapper.map(cartDetails, CartDetailResponse.class);

            if ("DEVICE".equals(cartDetails.getProductType())) {
                Device device = cartDetails.getDevice();
                if (device != null) {
                    cartDetailResponse.setDeviceId(device.getId());
                    cartDetailResponse.setProductImage(device.getMainImage());
                    cartDetailResponse.setProductName(device.getName());
                    cartDetailResponse.setProductCategory(device.getTypeDevice().getName());
                    cartDetailResponse.setColorName(device.getColorName());
                    cartDetailResponse.setProductPrice(device.getPrice());
                    cartDetailResponse.setProductQuantity(device.getQuantity());
                    cartDetailResponse.setSalePrice(device.getSale() != null ? device.getSalePrice() : null);
                    cartDetailResponse.setSalePriceDiscount(device.getSale() != null ? device.getSale().getDiscount() : null);
                    cartDetailResponse.setTotalPrice(calculateTotalPrice(device.getPrice(), cartDetails.getQuantity()));
                    cartDetailResponse.setTotalPriceDiscount(calculateDiscountedTotalPrice(device.getPrice(), device.getSale(), cartDetails.getQuantity()));
                }
            } else if ("ACCESSORY".equals(cartDetails.getProductType())) {
                Accessory accessory = cartDetails.getAccessory();
                if (accessory != null) {
                    cartDetailResponse.setAccessoryId(accessory.getId());
                    cartDetailResponse.setProductImage(accessory.getMainImage());
                    cartDetailResponse.setProductName(accessory.getName());
                    cartDetailResponse.setProductCategory(accessory.getAccessoryCategory().getName());
                    cartDetailResponse.setColorName(accessory.getColorName());
                    cartDetailResponse.setProductPrice(accessory.getPrice());
                    cartDetailResponse.setProductQuantity(accessory.getQuantity());
                    cartDetailResponse.setSalePrice(accessory.getSale() != null ? accessory.getSalePrice() : null);
                    cartDetailResponse.setSalePriceDiscount(accessory.getSale() != null ? accessory.getSale().getDiscount() : null);
                    cartDetailResponse.setTotalPrice(calculateTotalPrice(accessory.getPrice(), cartDetails.getQuantity()));
                    cartDetailResponse.setTotalPriceDiscount(calculateDiscountedTotalPrice(accessory.getPrice(), accessory.getSale(), cartDetails.getQuantity()));
                }
            }

            cartDetailResponses.add(cartDetailResponse);
        }

        return cartDetailResponses;
    }
    @Override
    @Transactional
    public void deleteCartDetailsById(Integer id) {
        Optional<CartDetails> cartDetailsOptional = cartDetailsRepository.findById(id);
        if (cartDetailsOptional.isPresent()) {
            CartDetails cartDetails = cartDetailsOptional.get();

            // Kiểm tra và xóa theo loại sản phẩm
            if ("DEVICE".equals(cartDetails.getProductType())) {
                cartDetailsRepository.deleteByDeviceId(cartDetails.getDevice().getId());
            } else if ("ACCESSORY".equals(cartDetails.getProductType())) {
                cartDetailsRepository.deleteByAccessoryId(cartDetails.getAccessory().getId());
            } else {
                throw new IllegalArgumentException("Unsupported product type: " + cartDetails.getProductType());
            }
        } else {
            throw new RuntimeException("Cart details not found for id: " + id);
        }
    }
    @Override
    public Map<String, String> checkCart(String email, int[] cartIds) {
        Cart cart = cartRepository.findByUserEmail(email);
        Map<String, String> errors = new HashMap<>();
        for (CartDetails cartDetails : cart.getCartDetails()) {
            if (isCartDetailInCartIds(cartDetails, cartIds)) {
                if (cartDetails.getProductType().equals("DEVICE")) {
                    Device device = deviceRepository.findById(cartDetails.getDevice().getId()).orElse(null);
                    if (device == null || device.getQuantity() < cartDetails.getQuantity()) {
                        errors.put(device.getName(), "Không đủ số lượng cho sản phẩm này!");
                    }
                } else if (cartDetails.getProductType().equals("ACCESSORY")) {
                    Accessory accessory = accessoryRepository.findById(cartDetails.getAccessory().getId()).orElse(null);
                    if (accessory == null || accessory.getQuantity() < cartDetails.getQuantity()) {
                        errors.put(accessory.getName(), "Không đủ số lượng cho sản phẩm này!");
                    }
                }
            }
        }
        return errors;
    }

    private boolean isCartDetailInCartIds(CartDetails cartDetails, int[] cartIds) {
        for (int cartId : cartIds) {
            if (cartDetails.getId() == cartId) {
                return true;
            }
        }
        return false;
    }

    private Double calculateTotalPrice(Double price, Integer quantity) {
        return price * quantity;
    }

    private Double calculateDiscountedTotalPrice(Double price, Sale sale, Integer quantity) {
        if (sale != null) {
            Double discount = sale.getDiscount() / 100.0;
            Double discountedPrice = price - (price * discount);
            return discountedPrice * quantity;
        }
        return calculateTotalPrice(price, quantity);
    }
}

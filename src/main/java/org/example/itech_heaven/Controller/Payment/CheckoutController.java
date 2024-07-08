package org.example.itech_heaven.Controller.Payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.DTO.request.CartDetailDTO;
import org.example.itech_heaven.DTO.request.CartDetailsRequest;
import org.example.itech_heaven.Entity.*;
import org.example.itech_heaven.Service.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final DeviceTypeSerivce deviceTypeSerivce;
    private final CartService cartService;
    private final CartDetailsService cartDetailsService;
    private final OrderService orderService;
    private final EmailSenderService emailSenderService;
    @GetMapping()
    public String getFormCheckout(Model model,
                                  @RequestParam(name = "cartData") String  cartData,
                                  @RequestParam(name = "selectedCartIds")int[] cartIds,
                                  @RequestParam("totalPrice")double totalPrice){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            CartDetailDTO[] cartdetailData = objectMapper.readValue(cartData, CartDetailDTO[].class);
            cartDetailsService.updateListCartDetail(cartdetailData);


        }  catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        List<TypeDevice> devices = deviceTypeSerivce.getAllTypeDevices();
        model.addAttribute("cartIds", cartIds);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("typeDevices", devices);
        model.addAttribute("user", user);
        return "checkout";
    }


    @PostMapping("/confirm")
    public String confirmCheckout(Model model,
                                  HttpSession session,
                                  @RequestParam("full-name")String fullName,
                                  @RequestParam("phone")String phone,
                                  @RequestParam(name = "tinh", defaultValue = "")String tinh,
                                  @RequestParam(name = "quan", defaultValue = "")String quan,
                                  @RequestParam(name = "phuong", defaultValue = "")String phuong,
                                  @RequestParam("address")String address,
                                  @RequestParam("email")String email,
                                  @RequestParam("amount")Long totalPrice,
                                  @RequestParam(name = "cartIds")int[] cartIds,
                                  @RequestParam("shipping")long shipping
                                  ){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Map<String,String> errors = cartService.checkCart(user.getEmail(), cartIds);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "sold-out";
        }
        session.setAttribute("fullName", fullName);
        session.setAttribute("phone", phone);
        session.setAttribute("address", tinh + " " + quan +" " + phuong +" " + address);
        session.setAttribute("email", email);
        session.setAttribute("cartIds", cartIds);
        session.setAttribute("amount", totalPrice);
        session.setAttribute("shipping", shipping);
        model.addAttribute("amount", totalPrice);




        return "payment-redirect";

    }

    @GetMapping("/success")
    public String successCheckout(Model model, HttpSession session,@RequestParam("vnp_TransactionStatus")String status ) throws MessagingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        String error = "";

        if (status.equals("01")){
            error = ("Giao dịch chưa hoàn tất");
        } else if (status.equals("02")) {
            error = ("Giao dịch bị lỗi");
        } else if (status.equals("00")) {


            int[] cartIds = (int[]) session.getAttribute("cartIds");
            long totalPrice = (long) session.getAttribute("amount");
            long shipping = (long) session.getAttribute("shipping");
            String email = (String) session.getAttribute("email");
            String phone = (String) session.getAttribute("phone");
            String address = (String) session.getAttribute("address");
            String fullName = (String) session.getAttribute("fullName");
            String subContent = "";
            for (int id : cartIds){
                CartDetails cartDetails = cartDetailsService.findById(id);
                if (cartDetails.getProductType().equals("DEVICE")){
                    subContent += "                <tr>\n" +
                            "                    <td>"+ cartDetails.getDevice().getName()+"</td>\n" +
                            "                    <td>"+ cartDetails.getDevice().getColorName()+"</td>\n" +
                            "                    <td>"+cartDetails.getQuantity() +"</td>\n" +
                            "                    <td>"+cartDetails.getDevice().getPrice()+"  VNĐ</td>\n" +
                            "                </tr>\n";
                }else{
                    subContent += "                <tr>\n" +
                            "                    <td>"+ cartDetails.getAccessory().getName()+"</td>\n" +
                            "                    <td>"+ cartDetails.getAccessory().getColorName()+"</td>\n" +
                            "                    <td>"+cartDetails.getQuantity() +"</td>\n" +
                            "                    <td>"+cartDetails.getAccessory().getPrice()+"  VNĐ</td>\n" +
                            "                </tr>\n";
                }
            }


            Order order = new Order();
            order.setDate(LocalDateTime.now());
            order.setStatus("Đã thanh toán");
            order.setUser(user);
            order.setAddressReceiver(address);
            order.setNameReceiver(fullName);
            order.setPhoneReceiver(phone);
            order.setTotalMoney(totalPrice);
            order.setEmailReceiver(email);
            order = orderService.addOrder(order, cartIds);

            String subject = "Đơn hàng thành công!";
            String to = user.getEmail();
            String htmlContent = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Order Confirmation</title>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "            margin: 0;\n" +
                    "            padding: 0;\n" +
                    "            background-color: #f4f4f4;\n" +
                    "        }\n" +
                    "        .email-container {\n" +
                    "            max-width: 600px;\n" +
                    "            margin: 0 auto;\n" +
                    "            background-color: #ffffff;\n" +
                    "            border: 1px solid #e0e0e0;\n" +
                    "            border-radius: 10px;\n" +
                    "            overflow: hidden;\n" +
                    "        }\n" +
                    "        .email-header {\n" +
                    "            background-color: #e0b3ff;\n" +
                    "            color: #ffffff;\n" +
                    "            padding: 20px;\n" +
                    "            text-align: center;\n" +
                    "            font-size: 24px;\n" +
                    "            font-weight: bold;\n" +
                    "        }\n" +
                    "        .email-body {\n" +
                    "            padding: 20px;\n" +
                    "            font-size: 16px;\n" +
                    "            line-height: 1.5;\n" +
                    "            color: #333333;\n" +
                    "        }\n" +
                    "        .email-body table {\n" +
                    "            width: 100%;\n" +
                    "            border-collapse: collapse;\n" +
                    "            margin-top: 20px;\n" +
                    "        }\n" +
                    "        .email-body table, .email-body th, .email-body td {\n" +
                    "            border: 1px solid #dddddd;\n" +
                    "            padding: 8px;\n" +
                    "            text-align: left;\n" +
                    "        }\n" +
                    "        .email-body th {\n" +
                    "            background-color: #f2f2f2;\n" +
                    "        }\n" +
                    "        .email-footer {\n" +
                    "            padding: 20px;\n" +
                    "            text-align: center;\n" +
                    "            font-size: 14px;\n" +
                    "            color: #666666;\n" +
                    "        }\n" +
                    "        .email-footer a {\n" +
                    "            color: #e0b3ff;\n" +
                    "            text-decoration: none;\n" +
                    "        }\n" +
                    "        .email-footer a:hover {\n" +
                    "            text-decoration: underline;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div class=\"email-container\">\n" +
                    "    <div class=\"email-header\">\n" +
                    "        Đơn Hàng Thành Công\n" +
                    "    </div>\n" +
                    "    <div class=\"email-body\">\n" +
                    "        <p>Xin chào " +user.getFirstname() + " " + user.getLastname() +",</p>\n" +
                    "        <p>Cảm ơn bạn đã đặt hàng tại iTech Heaven! Đơn hàng của bạn đã được xác nhận và đang được xử lý.</p>\n" +
                    "        <p><strong>Thông tin đơn hàng:</strong></p>\n" +
                    "        <ul>\n" +
                    "            <li><strong>ID đơn hàng:</strong> "+ order.getId()+"</li>\n" +
                    "            <li><strong>Ngày đặt hàng:</strong> "+ order.getDate()+"</li>\n" +
                    "            <li><strong>Phí vận chuyển:</strong> "+ shipping+"  VNĐ</li>\n" +
                    "            <li><strong>Tổng tiền:</strong> "+order.getTotalMoney()+"  VNĐ</li>\n" +
                    "        </ul>\n" +
                    "        <div>\n" +
                    "            <table>\n" +
                    "                <thead>\n" +
                    "                <tr>\n" +
                    "                    <th>Tên sản phẩm</th>\n" +
                    "                    <th>Màu sắc</th>\n" +
                    "                    <th>Số lượng</th>\n" +
                    "                    <th>Giá tiền</th>\n" +
                    "                </tr>\n" +
                    "                </thead>\n" +
                    "                <tbody>\n";
            htmlContent += subContent;

            htmlContent +=        "                </tbody>\n" +
                    "                <tfoot>\n" +
                    "                <tr>\n" +
                    "                    <td colspan=\"3\" style=\"text-align: right;\"><strong>Tổng tiền:</strong></td>\n" +
                    "                    <td>"+order.getTotalMoney()+" VNĐ</td>\n" +
                    "                </tr>\n" +
                    "                </tfoot>\n" +
                    "            </table>\n" +
                    "        </div>\n" +
                    "        <p>Bạn có thể theo dõi tình trạng đơn hàng của mình bằng cách đăng nhập vào tài khoản trên trang web của chúng tôi.</p>\n" +
                    "        <p>Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi qua email <a href=\"mailto:support@itechheaven.com\">support@itechheaven.com</a>.</p>\n" +
                    "        <p>Chúng tôi hy vọng bạn sẽ hài lòng với sản phẩm của mình!</p>\n" +
                    "        <p>Trân trọng,</p>\n" +
                    "        <p>Đội ngũ iTech Heaven</p>\n" +
                    "    </div>\n" +
                    "    <div class=\"email-footer\">\n" +
                    "        <p>&copy; 2024 iTech Heaven. All rights reserved.</p>\n" +
                    "        <p><a href=\"https://www.itechheaven.com\">www.itechheaven.com</a></p>\n" +
                    "    </div>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>\n";

            emailSenderService.sendEmail(to,subject ,htmlContent );
        }


        model.addAttribute("error", error);


        return "checkout-success";
    }


    @PostMapping("/save-order")
    public String saveOrder(Model model,
                            @RequestParam("full-name")String fullName,
                            @RequestParam("phone")String phone,
                            @RequestParam(name = "tinh", defaultValue = "")String tinh,
                            @RequestParam(name = "quan", defaultValue = "")String quan,
                            @RequestParam(name = "phuong", defaultValue = "")String phuong,
                            @RequestParam("address")String address,
                            @RequestParam("email")String email,
                            @RequestParam("amount")long totalPrice,
                            @RequestParam(name = "cartIds")int[] cartIds,
                            @RequestParam("shipping")long shipping){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();


        Map<String,String> errors = cartService.checkCart(user.getEmail(), cartIds);
        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            return "sold-out";
        }
        String subContent = "";
        for (int id : cartIds){
            CartDetails cartDetails = cartDetailsService.findById(id);
            if (cartDetails.getProductType().equals("DEVICE")){
                subContent += "                <tr>\n" +
                        "                    <td>"+ cartDetails.getDevice().getName()+"</td>\n" +
                        "                    <td>"+ cartDetails.getDevice().getColorName()+"</td>\n" +
                        "                    <td>"+cartDetails.getQuantity() +"</td>\n" +
                        "                    <td>"+cartDetails.getDevice().getPrice()+" VNĐ</td>\n" +
                        "                </tr>\n";
            }else{
                subContent += "                <tr>\n" +
                        "                    <td>"+ cartDetails.getAccessory().getName()+"</td>\n" +
                        "                    <td>"+ cartDetails.getAccessory().getColorName()+"</td>\n" +
                        "                    <td>"+cartDetails.getQuantity() +"</td>\n" +
                        "                    <td>"+cartDetails.getAccessory().getPrice()+" VNĐ</td>\n" +
                        "                </tr>\n";
            }
        }

        Order order = new Order();
        order.setDate(LocalDateTime.now());
        order.setStatus("Chờ xủ lý");
        order.setUser(user);
        order.setAddressReceiver(tinh + " " + quan +" " + phuong +" " + address);
        order.setNameReceiver(fullName);
        order.setPhoneReceiver(phone);
        order.setTotalMoney(totalPrice);
        order.setEmailReceiver(email);
        try {
            order= orderService.addOrderPending(order, cartIds);
            String subject = "Đơn hàng thành công!";
            String to = user.getEmail();
            String htmlContent = "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <title>Order Confirmation</title>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "            margin: 0;\n" +
                    "            padding: 0;\n" +
                    "            background-color: #f4f4f4;\n" +
                    "        }\n" +
                    "        .email-container {\n" +
                    "            max-width: 600px;\n" +
                    "            margin: 0 auto;\n" +
                    "            background-color: #ffffff;\n" +
                    "            border: 1px solid #e0e0e0;\n" +
                    "            border-radius: 10px;\n" +
                    "            overflow: hidden;\n" +
                    "        }\n" +
                    "        .email-header {\n" +
                    "            background-color: #e0b3ff;\n" +
                    "            color: #ffffff;\n" +
                    "            padding: 20px;\n" +
                    "            text-align: center;\n" +
                    "            font-size: 24px;\n" +
                    "            font-weight: bold;\n" +
                    "        }\n" +
                    "        .email-body {\n" +
                    "            padding: 20px;\n" +
                    "            font-size: 16px;\n" +
                    "            line-height: 1.5;\n" +
                    "            color: #333333;\n" +
                    "        }\n" +
                    "        .email-body table {\n" +
                    "            width: 100%;\n" +
                    "            border-collapse: collapse;\n" +
                    "            margin-top: 20px;\n" +
                    "        }\n" +
                    "        .email-body table, .email-body th, .email-body td {\n" +
                    "            border: 1px solid #dddddd;\n" +
                    "            padding: 8px;\n" +
                    "            text-align: left;\n" +
                    "        }\n" +
                    "        .email-body th {\n" +
                    "            background-color: #f2f2f2;\n" +
                    "        }\n" +
                    "        .email-footer {\n" +
                    "            padding: 20px;\n" +
                    "            text-align: center;\n" +
                    "            font-size: 14px;\n" +
                    "            color: #666666;\n" +
                    "        }\n" +
                    "        .email-footer a {\n" +
                    "            color: #e0b3ff;\n" +
                    "            text-decoration: none;\n" +
                    "        }\n" +
                    "        .email-footer a:hover {\n" +
                    "            text-decoration: underline;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<div class=\"email-container\">\n" +
                    "    <div class=\"email-header\">\n" +
                    "        Đơn Hàng Thành Công\n" +
                    "    </div>\n" +
                    "    <div class=\"email-body\">\n" +
                    "        <p>Xin chào " +user.getFirstname() + " " + user.getLastname() +",</p>\n" +
                    "        <p>Cảm ơn bạn đã đặt hàng tại iTech Heaven! Đơn hàng của bạn đã được xác nhận và đang được xử lý.</p>\n" +
                    "        <p><strong>Thông tin đơn hàng:</strong></p>\n" +
                    "        <ul>\n" +
                    "            <li><strong>ID đơn hàng:</strong> "+ order.getId()+"</li>\n" +
                    "            <li><strong>Ngày đặt hàng:</strong> "+ order.getDate()+"</li>\n" +
                    "            <li><strong>Phí vận chuyển:</strong> "+ shipping+"</li>\n" +
                    "            <li><strong>Tổng tiền:</strong> "+order.getTotalMoney()+" VNĐ</li>\n" +
                    "        </ul>\n" +
                    "        <div>\n" +
                    "            <table>\n" +
                    "                <thead>\n" +
                    "                <tr>\n" +
                    "                    <th>Tên sản phẩm</th>\n" +
                    "                    <th>Màu sắc</th>\n" +
                    "                    <th>Số lượng</th>\n" +
                    "                    <th>Giá tiền</th>\n" +
                    "                </tr>\n" +
                    "                </thead>\n" +
                    "                <tbody>\n";
            htmlContent += subContent;

            htmlContent +=        "                </tbody>\n" +
                    "                <tfoot>\n" +
                    "                <tr>\n" +
                    "                    <td colspan=\"3\" style=\"text-align: right;\"><strong>Tổng tiền:</strong></td>\n" +
                    "                    <td>"+order.getTotalMoney()+" VNĐ</td>\n" +
                    "                </tr>\n" +
                    "                </tfoot>\n" +
                    "            </table>\n" +
                    "        </div>\n" +
                    "        <p>Bạn có thể theo dõi tình trạng đơn hàng của mình bằng cách đăng nhập vào tài khoản trên trang web của chúng tôi.</p>\n" +
                    "        <p>Nếu bạn có bất kỳ câu hỏi nào, vui lòng liên hệ với chúng tôi qua email <a href=\"mailto:support@itechheaven.com\">support@itechheaven.com</a>.</p>\n" +
                    "        <p>Chúng tôi hy vọng bạn sẽ hài lòng với sản phẩm của mình!</p>\n" +
                    "        <p>Trân trọng,</p>\n" +
                    "        <p>Đội ngũ iTech Heaven</p>\n" +
                    "    </div>\n" +
                    "    <div class=\"email-footer\">\n" +
                    "        <p>&copy; 2024 iTech Heaven. All rights reserved.</p>\n" +
                    "        <p><a href=\"https://www.itechheaven.com\">www.itechheaven.com</a></p>\n" +
                    "    </div>\n" +
                    "</div>\n" +
                    "</body>\n" +
                    "</html>\n";

            emailSenderService.sendEmail(to,subject ,htmlContent );
        }catch (Exception e){
            model.addAttribute("error", "Lỗi trong quá trình đặt đơn vui lòng liên hệ shop để biết thêm thông tin chi tiết!");
            return "order-success";
        }

        model.addAttribute("error", "");

        return "order-success";
    }
}

package org.example.itech_heaven.Controller.Home;


import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.DTO.ContactUsDTO;
import org.example.itech_heaven.Entity.TypeDevice;
import org.example.itech_heaven.Service.DeviceTypeSerivce;
import org.example.itech_heaven.Service.EmailSenderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class ContactController {

    private final EmailSenderService emailSenderService;
    private final DeviceTypeSerivce deviceTypeSerivce;

    @GetMapping("/contact")
    public String getContact(Model model){
        List<TypeDevice> devices = deviceTypeSerivce.getAllTypeDevices();
        model.addAttribute("typeDevices", devices);
        ContactUsDTO contactUsDTO = new ContactUsDTO();
        model.addAttribute("contactUsDTO", contactUsDTO);
        return "contact";
    }

    @PostMapping("/contact")
    public String processContact(@ModelAttribute("contactUsDTO") ContactUsDTO contactUsDTO, Model model) throws MessagingException {
        String message = contactUsDTO.getMessage();
        String subject = contactUsDTO.getSubject();
        String phone = contactUsDTO.getPhone();
        String to = contactUsDTO.getEmail();
        String name = contactUsDTO.getName();
        String contentToShop = "Khách hàng " + name + " muốn liên hệ với cửa hàng!" +
                                "<br/>Chủ đề: "+ subject +
                                "<br/>Số điện thoại: " + phone +
                                "<br/>Email: " + to +
                                "<br/>Nội dung: " + message
                ;

        String contentToCustomer =
                "<p>Chào " + name + ",</p>" +
                        "<p>Cảm ơn bạn đã liên hệ với cửa hàng của chúng tôi! Chúng tôi rất vui mừng được nhận được thông điệp từ bạn.</p>" +
                        "<p>Chúng tôi sẽ xem xét nội dung của bạn và sẽ phản hồi bạn trong thời gian sớm nhất có thể. Trong khi đó, nếu bạn có bất kỳ câu hỏi hoặc yêu cầu khác, đừng ngần ngại liên hệ với chúng tôi.</p>" +
                        "<p>Cảm ơn bạn một lần nữa đã chọn cửa hàng của chúng tôi và chúng tôi mong được phục vụ bạn!</p>" +
                        "<p>Trân trọng,</p>" +
                        "<p>iTech Heaven</p>";

        emailSenderService.sendEmail(contactUsDTO.getEmail(), "Cảm ơn bạn đã liên hệ với cửa hàng!", contentToCustomer);
        emailSenderService.sendEmail("ngminhtuan1128@gmail.com", "Liên hệ từ khách hàng", contentToShop);
        return "redirect:/mail-success";
    }

    @GetMapping("/mail-success")
    public String getMailSuccess(Model model){

        return "mail-success";
    }
}

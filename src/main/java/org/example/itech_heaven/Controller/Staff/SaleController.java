package org.example.itech_heaven.Controller.Staff;

import jakarta.validation.Valid;
import org.example.itech_heaven.Entity.Accessory;
import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.Sale;
import org.example.itech_heaven.Service.AccessoryService;
import org.example.itech_heaven.Service.DeviceService;
import org.example.itech_heaven.Service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/manage-sale")
public class SaleController {

    @Autowired
    private SaleService saleService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private AccessoryService accessoryService;

    private static final String UPLOADED_FOLDER = "src/main/resources/static/images/uploaded/";

    @GetMapping("/listSale")
    public String GetALlSale(Model model) {
        List<Sale> saleList = saleService.getSaleList();
        model.addAttribute("saleList", saleList);
        return "manage-sale";
    }

    @GetMapping("/form-add-sale")
    public String Form(Model model) {
        Sale sale = new Sale();
        model.addAttribute("sale", sale);
        return "form-add-sale";
    }

    @GetMapping("/update/{saleId}")
    public String UpdateSale(Model model, @PathVariable("saleId") int saleId) {
        Sale sale = saleService.getSaleById(saleId);
        model.addAttribute("sale", sale);
        return "form-add-sale";
    }

    @PostMapping("/save")
    public String SaveSale( Model model,
                            @ModelAttribute("sale") Sale sale,
                            @RequestParam("mainImageFile") MultipartFile mainImageFile) {

        if (!mainImageFile.isEmpty()) {
            try {
                String originalFilename = mainImageFile.getOriginalFilename().toLowerCase();

                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                String randomFilename = UUID.randomUUID().toString() + fileExtension;

                byte[] bytes = mainImageFile.getBytes();
                File file = new File(UPLOADED_FOLDER + randomFilename);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(bytes);
                }

                sale.setImage("/images/uploaded/" + randomFilename);
            } catch (IOException e) {
                return "form-add-sale";
            }
        }

        saleService.saveSale(sale);
        return "redirect:/manage-sale/listSale";
    }

    @GetMapping("/delete/{saleId}")
    public String DeleteSale(Model model, @PathVariable("saleId") int saleId) {
        List<Device> deviceList = deviceService.fineDevicesBySale(saleId);
        for (Device device : deviceList) {
            device.setSale(null);
            deviceService.saveDevice(device);
        }
        List<Accessory> accessoryList = accessoryService.getBySaleId(saleId);
        for (Accessory accessory : accessoryList) {
            accessory.setSale(null);
            accessoryService.Save(accessory);
        }
        saleService.deleteSale(saleId);
        return "redirect:/manage-sale/listSale";
    }

    @PostMapping("/search")
    public String Search(Model model, @RequestParam("saleName") String saleName){
        model.addAttribute("saleList", saleService.findBySaleName(saleName));
        model.addAttribute("saleName", saleName);
        return "manage-sale";
    }
}

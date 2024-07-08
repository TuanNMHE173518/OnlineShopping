package org.example.itech_heaven.Controller.Staff;

import jakarta.validation.Valid;
import org.example.itech_heaven.Entity.*;
import org.example.itech_heaven.Repository.DeviceRepository;
import org.example.itech_heaven.Service.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/manage-device")
public class DeviceController {

    private static final String UPLOADED_FOLDER = "src/main/resources/static/images/uploaded/";

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private TypeDeviceService typeDeviceService;
    @Autowired
    private SaleService saleService;
    @Autowired
    private DeviceImageService deviceImageService;


    @GetMapping("/listDevice")
    public String ManageProductPage(Model model) {
        List<TypeDevice> typeDevicelist = typeDeviceService.getAllTypeDevice();
        List<Device> deviceList = deviceService.getDevices();
        model.addAttribute("deviceList", deviceList);
        model.addAttribute("typeDeviceList", typeDevicelist);
        return "manage-device";
    }

    @PostMapping("/search")
    public String SearchDeviceByName(Model model, @RequestParam(name = "deviceName") String deviceName, @RequestParam(value = "typeDeviceId", defaultValue = "0") int typeDeviceId) {
        List<TypeDevice> typeDevicelist = typeDeviceService.getAllTypeDevice();
        List<Device> deviceList = deviceService.getDevices();
        if (!deviceName.isBlank()) {
            if (typeDeviceId != 0) {
                deviceList = deviceService.findDevicesByTypeAndName(typeDeviceId, deviceName);
            } else {
                deviceList = deviceService.getDevicesByName(deviceName);
            }
        } else {
            if (typeDeviceId != 0) {
                deviceList = deviceService.getDevicesByType(typeDeviceId);
            }
        }
        model.addAttribute("deviceList", deviceList);
        model.addAttribute("typeDeviceList", typeDevicelist);
        model.addAttribute("deviceName", deviceName);
        model.addAttribute("typeDeviceId", typeDeviceId);

        return "manage-device";
    }

    @GetMapping("/search/{typeDeviceId}")
    public String SearchDeviceByTypeDeviceId(Model model, @PathVariable int typeDeviceId) {
        List<TypeDevice> typeDevicelist = typeDeviceService.getAllTypeDevice();
        List<Device> deviceList = deviceService.getDevices();
        if (typeDeviceId != 0) {
            deviceList = deviceService.getDevicesByType(typeDeviceId);
        }

        model.addAttribute("deviceList", deviceList);
        model.addAttribute("typeDeviceList", typeDevicelist);
        model.addAttribute("typeDeviceId", typeDeviceId);
        return "manage-device";
    }

    @GetMapping("/search/{typeDeviceId}/{deviceName}")
    public String SearchDeviceByTypeDeviceIdAndDeviceName(Model model, @PathVariable int typeDeviceId, @PathVariable String deviceName) {
        List<TypeDevice> typeDevicelist = typeDeviceService.getAllTypeDevice();
        List<Device> deviceList = deviceService.findDevicesByTypeAndName(typeDeviceId, deviceName);
        if (typeDeviceId == 0) {
            deviceList = deviceService.getDevicesByName(deviceName);
        }
        model.addAttribute("deviceList", deviceList);
        model.addAttribute("typeDeviceList", typeDevicelist);
        model.addAttribute("typeDeviceId", typeDeviceId);
        model.addAttribute("deviceName", deviceName);
        return "manage-device";
    }

    @GetMapping("/form-add-device")
    public String Form(Model model) {
        Device device = new Device();
        model.addAttribute("typeDeviceList", typeDeviceService.getAllTypeDevice());
        model.addAttribute("saleList", saleService.getSaleList());
        model.addAttribute("device", device);
        return "form-add-device";
    }

    @GetMapping("/update/{deviceId}")
    public String UpdateDevice(Model model, @PathVariable("deviceId") int deviceId) {
        Device device = deviceService.getDeviceById(deviceId);
        model.addAttribute("typeDeviceList", typeDeviceService.getAllTypeDevice());
        model.addAttribute("saleList", saleService.getSaleList());
        model.addAttribute("device", device);
        return "form-add-device";
    }

    @PostMapping("/save")
    public String SaveDevice(Model model,
                             @Valid @ModelAttribute("device") Device device,
                             @RequestParam("mainImageFile") MultipartFile mainImageFile,
                             @RequestParam("extraImages") List<MultipartFile> extraImages
    ) {
        List<TypeDevice> typeDevicelist = typeDeviceService.getAllTypeDevice();
        List<Sale> saleList = saleService.getSaleList();
        Device dev = deviceService.getDeviceById(device.getId());
        model.addAttribute("typeDeviceList", typeDevicelist);
        model.addAttribute("saleList", saleList);
        if (dev != null) {
            device.setMainImage(dev.getMainImage());
            device.setDeviceImages(dev.getDeviceImages());
        }

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

                device.setMainImage("/images/uploaded/" + randomFilename);
            } catch (IOException e) {
                return "form-add-device";
            }
        }

        if (!extraImages.get(0).isEmpty()) {
            List<DeviceImage> deviceImageList = new ArrayList<>();
            for (MultipartFile extraImage : extraImages) {
                try {
                    String originalFilename = extraImage.getOriginalFilename().toLowerCase();
                    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    String randomFilename = UUID.randomUUID().toString() + fileExtension;

                    byte[] bytes = extraImage.getBytes();
                    File file = new File(UPLOADED_FOLDER + randomFilename);
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        fos.write(bytes);
                    }
                    DeviceImage deviceImage = new DeviceImage(0, "/images/uploaded/" + randomFilename, device);
                    deviceImageList.add(deviceImage);

                } catch (IOException e) {
                    return "form-add-device";
                }
            }

            deviceImageService.deleteDeviceImageByDeviceId(device.getId());
            device.setDeviceImages(deviceImageList);
        }

        deviceService.saveDevice(device);
        return "redirect:/manage-device/listDevice";
    }

    @GetMapping("/delete/{deviceId}")
    public String DeleteDevice(Model model, @PathVariable("deviceId") int deviceId) {
        List<TypeDevice> typeDevicelist = typeDeviceService.getAllTypeDevice();
        List<Device> deviceList = deviceService.getDevices();
        deviceService.deleteDevice(deviceId);
        model.addAttribute("deviceList", deviceList);
        model.addAttribute("typeDeviceList", typeDevicelist);
        return "redirect:/manage-device/listDevice";
    }

}

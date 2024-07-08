package org.example.itech_heaven.Controller.Staff;

import jakarta.validation.Valid;
import org.example.itech_heaven.Entity.*;
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
@RequestMapping("/manage-accessory")
public class AccessoryController {

    private static final String UPLOADED_FOLDER = "src/main/resources/static/images/uploaded/";

    @Autowired
    private AccessoryService accessoryService;
    @Autowired
    private TypeDeviceService typeDeviceService;
    @Autowired
    private AccessoryCategoryService accessoryCategoryService;
    @Autowired
    private SaleService saleService;
    @Autowired
    private AccessoryImageService accessoryImageService;


    @GetMapping("/listAccessory")
    public String listAccessory(Model model) {
        List<TypeDevice> typeDeviceList = typeDeviceService.getAllTypeDevice();
        List<Accessory> accessoryList = accessoryService.getAllAccessory();
        List<AccessoryCategory> accessoryCategories = accessoryCategoryService.getAllAccessoryCategories();
        model.addAttribute("typeDeviceList", typeDeviceList);
        model.addAttribute("accessoryList", accessoryList);
        model.addAttribute("accessoryCategories", accessoryCategories);
        return "manage-accessory";
    }

    @GetMapping("/search/{accessoryCatId}/{typeDeviceId}")
    public String searchByAccessoryCatIdAndType(Model model,
                                                @PathVariable("accessoryCatId") int accessoryCatId,
                                                @PathVariable("typeDeviceId") int typeDeviceId) {
        List<TypeDevice> typeDeviceList = typeDeviceService.getAllTypeDevice();
        List<AccessoryCategory> accessoryCategories = accessoryCategoryService.getAllAccessoryCategories();
        List<Accessory> accessoryList;
        if (accessoryCatId == 0 && typeDeviceId == 0) {
            accessoryList = accessoryService.getAllAccessory();
        } else if (accessoryCatId == 0) {
            accessoryList = accessoryService.getAccessoryByTypeDeviceId(typeDeviceId);
        } else if (typeDeviceId == 0) {
            accessoryList = accessoryService.getAccessoryByType(accessoryCatId);
        } else {
            accessoryList = accessoryService.search(accessoryCatId, typeDeviceId, "");
        }

        model.addAttribute("typeDeviceList", typeDeviceList);
        model.addAttribute("accessoryList", accessoryList);
        model.addAttribute("accessoryCategories", accessoryCategories);
        model.addAttribute("accessoryCatId", accessoryCatId);
        model.addAttribute("typeDeviceId", typeDeviceId);
        model.addAttribute("accessoryName", "");
        return "manage-accessory";
    }

    @GetMapping("/search/{accessoryCatId}/{typeDeviceId}/{accessoryName}")
    public String search(Model model,
                         @PathVariable("accessoryCatId") int accessoryCatId,
                         @PathVariable("typeDeviceId") int typeDeviceId,
                         @PathVariable("accessoryName") String accessoryName) {
        List<TypeDevice> typeDeviceList = typeDeviceService.getAllTypeDevice();
        List<AccessoryCategory> accessoryCategories = accessoryCategoryService.getAllAccessoryCategories();
        List<Accessory> accessoryList;

        if (accessoryCatId == 0 && typeDeviceId == 0) {
            accessoryList = accessoryService.getAccessoryByName(accessoryName);
        } else if (accessoryCatId == 0) {
            accessoryList = accessoryService.searchByTypeDeviceAndName(typeDeviceId, accessoryName);
        } else if (typeDeviceId == 0) {
            accessoryList = accessoryService.searchByAccessoryIdAndName(accessoryCatId, accessoryName);
        } else {
            accessoryList = accessoryService.search(accessoryCatId, typeDeviceId, accessoryName);
        }

        model.addAttribute("typeDeviceList", typeDeviceList);
        model.addAttribute("accessoryList", accessoryList);
        model.addAttribute("accessoryCategories", accessoryCategories);
        model.addAttribute("accessoryCatId", accessoryCatId);
        model.addAttribute("typeDeviceId", typeDeviceId);
        model.addAttribute("accessoryName", accessoryName);
        return "manage-accessory";
    }

    @PostMapping("/search")
    public String searchForm(Model model,
                             @RequestParam(value = "accessoryCatId", defaultValue = "0") int accessoryCatId,
                             @RequestParam(value = "typeDeviceId", defaultValue = "0") int typeDeviceId,
                             @RequestParam("accessoryName") String accessoryName) {
        List<TypeDevice> typeDeviceList = typeDeviceService.getAllTypeDevice();
        List<AccessoryCategory> accessoryCategories = accessoryCategoryService.getAllAccessoryCategories();
        List<Accessory> accessoryList = new ArrayList<>();
        if (accessoryName.isEmpty()) {
            accessoryList = accessoryService.getAllAccessory();
            if (accessoryCatId != 0) {
                accessoryList = accessoryService.searchByAccessoryIdAndName(accessoryCatId, accessoryName);
            }
            if (typeDeviceId != 0) {
                accessoryList = accessoryService.searchByTypeDeviceAndName(typeDeviceId, accessoryName);
            }
            if (accessoryCatId != 0 && typeDeviceId != 0) {
                accessoryList = accessoryService.search(accessoryCatId, typeDeviceId, accessoryName);
            }
        } else {
            if (accessoryCatId == 0 && typeDeviceId == 0) {
                accessoryList = accessoryService.getAccessoryByName(accessoryName);
            }
            if (accessoryCatId != 0) {
                accessoryList = accessoryService.searchByAccessoryIdAndName(accessoryCatId, accessoryName);
            }
            if (typeDeviceId != 0) {
                accessoryList = accessoryService.searchByTypeDeviceAndName(typeDeviceId, accessoryName);
            }
            if (accessoryCatId != 0 && typeDeviceId != 0) {
                accessoryList = accessoryService.search(accessoryCatId, typeDeviceId, accessoryName);
            }
        }
        model.addAttribute("typeDeviceList", typeDeviceList);
        model.addAttribute("accessoryList", accessoryList);
        model.addAttribute("accessoryCategories", accessoryCategories);
        model.addAttribute("accessoryCatId", accessoryCatId);
        model.addAttribute("typeDeviceId", typeDeviceId);
        model.addAttribute("accessoryName", accessoryName);
        return "manage-accessory";
    }

    @GetMapping("/form-add-accessory")
    public String Form(Model model) {
        Accessory accessory = new Accessory();
        model.addAttribute("typeDeviceList", typeDeviceService.getAllTypeDevice());
        model.addAttribute("saleList", saleService.getSaleList());
        model.addAttribute("accessoryCategories", accessoryCategoryService.getAllAccessoryCategories());
        model.addAttribute("accessory", accessory);
        return "form-add-accessory";
    }

    @GetMapping("/update/{accessoryId}")
    public String UpdateAccessory(Model model, @PathVariable("accessoryId") int accessoryId) {
        Accessory accessory = accessoryService.getAccessoryById(accessoryId);
        model.addAttribute("typeDeviceList", typeDeviceService.getAllTypeDevice());
        model.addAttribute("saleList", saleService.getSaleList());
        model.addAttribute("accessory", accessory);
        model.addAttribute("accessoryCategories", accessoryCategoryService.getAllAccessoryCategories());
        return "form-add-accessory";
    }

    @PostMapping("/save")
    public String SaveAccessory(Model model,
                                @Valid @ModelAttribute("accessory") Accessory accessory,
                                @RequestParam(value = "typeDeviceIdList", defaultValue = "") List<Integer> typeDeviceIdList,
                                @RequestParam(value = "mainImageFile") MultipartFile mainImageFile,
                                @RequestParam("extraImages") List<MultipartFile> extraImages
    ) {
        Accessory acc = accessoryService.getAccessoryById( accessory.getId());
        model.addAttribute("typeDeviceList", typeDeviceService.getAllTypeDevice());
        model.addAttribute("saleList", saleService.getSaleList());
        model.addAttribute("accessoryCategories", accessoryCategoryService.getAllAccessoryCategories());
        if (acc != null) {
            accessory.setMainImage(acc.getMainImage());
            accessory.setAccessoryImages(acc.getAccessoryImages());
        }
        accessory.setTypeDevices(typeDeviceService.getTypeDeviceByListTypeId(typeDeviceIdList));


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

                accessory.setMainImage("/images/uploaded/" + randomFilename);

            } catch (IOException e) {
                return "form-add-accessory";
            }
        }

        if (!extraImages.get(0).isEmpty()) {
            List<AccessoryImage> accessoryImageList = new ArrayList<>();
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
                    AccessoryImage accessoryImage = new AccessoryImage(0, "/images/uploaded/" + randomFilename, accessory);
                    accessoryImageList.add(accessoryImage);
                } catch (IOException e) {
                    return "form-add-accessory";
                }
            }

            accessoryImageService.deleteByAccessoryId(accessory.getId());
            accessory.setAccessoryImages(accessoryImageList);
        }

        accessoryService.SaveAccessory(accessory, typeDeviceIdList);
        return "redirect:/manage-accessory/listAccessory";
    }


    @GetMapping("/delete/{accessoryId}")
    public String DeleteAccessory(Model model, @PathVariable("accessoryId") int accessoryId) {
        Accessory accessory = accessoryService.getAccessoryById(accessoryId);
        accessory.setTypeDevices(null);
        accessoryService.Save(accessory);
        accessoryService.DeleteAccessoryById(accessoryId);
        return "redirect:/manage-accessory/listAccessory";
    }

}

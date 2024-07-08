package org.example.itech_heaven.Controller.Accessory;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.Accessory;
import org.example.itech_heaven.Entity.AccessoryCategory;
import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.TypeDevice;
import org.example.itech_heaven.Repository.AccessoryRepository;
import org.example.itech_heaven.Service.AccessoryCategoryService;
import org.example.itech_heaven.Service.AccessoryService;
import org.example.itech_heaven.Service.DeviceTypeSerivce;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@RequiredArgsConstructor
@Controller
@RequestMapping("/accessory")
public class AccessoriesController {
    private final AccessoryService accessoryService;
    private  final DeviceTypeSerivce deviceTypeSerivce;
    @GetMapping("")
    public String showProductPage(
             @RequestParam(name = "sort",defaultValue = "nameAsc") String sort
            ,@RequestParam(name = "page",defaultValue = "1") Integer page ,Model model) {

        Page<Accessory> accessoriesListPage = accessoryService.getAllAccessory(buildPageable(page, sort));
        Integer totalPage = accessoriesListPage.getTotalPages();
        List<Accessory> accessoryList = accessoriesListPage.getContent();
        model.addAttribute("sort",sort);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        setDefaultAccessory(model, accessoryList);
        return "accessory";
    }


    private Pageable buildPageable(Integer page, String sortBy) {
        Sort sort = switch (sortBy) {
            case "priceAsc" -> Sort.by("price").ascending();
            case "priceDesc" -> Sort.by("price").descending();
            case "nameDesc" -> Sort.by("name").descending();
            default -> Sort.by("name").ascending();
        };
        return PageRequest.of(page - 1, 12, sort);
    }

    private void setDefaultAccessory(Model model, List<Accessory> accessoryList) {
        List<TypeDevice> typeDevices = deviceTypeSerivce.getAllTypeDevices();
        model.addAttribute("accessoryList", accessoryList);
        model.addAttribute("typeDevices", typeDevices);
    }
}

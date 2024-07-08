package org.example.itech_heaven.Controller.Product;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.TypeDevice;
import org.example.itech_heaven.Service.DeviceService;
import org.example.itech_heaven.Service.DeviceTypeSerivce;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/devices")
public class ProductController {
    private final DeviceTypeSerivce deviceTypeSerivce;
    private final DeviceService deviceService;


    @GetMapping("/{id}")
    public String showProductPage(@PathVariable("id") Integer id
                                , @RequestParam(name = "sort",defaultValue = "nameAsc") String sort
                                ,@RequestParam(name = "page",defaultValue = "1") Integer page ,Model model) {

        Page<Device> deviceListPage = deviceService.getAllDevicesByTypeDeviceId(
                id, buildPageable(page, sort));
        Integer totalPage = deviceListPage.getTotalPages();
        List<Device> deviceList = deviceListPage.getContent();
        model.addAttribute("sort",sort);
        model.addAttribute("typeDeviceId", id);
        model.addAttribute("page", page);
        model.addAttribute("totalPage", totalPage);
        setDefaultProduct(model, deviceList);
        return "device";
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

    private void setDefaultProduct(Model model, List<Device> deviceList) {
        List<TypeDevice> devices = deviceTypeSerivce.getAllTypeDevices();
        model.addAttribute("typeDevices", devices);
        model.addAttribute("deviceList", deviceList);
    }

}

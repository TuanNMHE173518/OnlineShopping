package org.example.itech_heaven.Controller.Product;

import lombok.RequiredArgsConstructor;
import org.example.itech_heaven.DTO.AccessorySaleDTO;
import org.example.itech_heaven.DTO.DeviceSaleDTO;
import org.example.itech_heaven.Entity.AccessoryCategory;
import org.example.itech_heaven.Entity.Device;
import org.example.itech_heaven.Entity.TypeDevice;
import org.example.itech_heaven.Service.AccessoryCategoryService;
import org.example.itech_heaven.Service.AccessoryService;
import org.example.itech_heaven.Service.DeviceService;
import org.example.itech_heaven.Service.DeviceTypeSerivce;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class ProductSaleController {

   private final DeviceService deviceService;
   private final DeviceTypeSerivce deviceTypeSerivce;
   private final AccessoryService accessoryService;
   private final AccessoryCategoryService accessoryCategoryService;

   @GetMapping("product-sale")
   public String getSaleProducts(Model model,
                                 @RequestParam(name = "pageNo", defaultValue = "0")int pageNo,
                                 @RequestParam(name = "search",defaultValue = "")String search,
                                 @RequestParam(name = "type-device", defaultValue = "0") int typeDevice,
                                 @RequestParam(name = "sort",defaultValue = "desc")String sort)
   {
      model.addAttribute("typedevicecheck", typeDevice);
      model.addAttribute("search", search);
      model.addAttribute("sort",sort);


      List<TypeDevice> typeDevices = deviceTypeSerivce.getAllTypeDevices();
      model.addAttribute("typeDevices", typeDevices);
      Page<DeviceSaleDTO> saleDevices = deviceService.getSaleDevices(pageNo, search, typeDevice,sort);
      model.addAttribute("currentPage", pageNo);
      model.addAttribute("totalPage", saleDevices.getTotalPages());


      model.addAttribute("saleDevices", saleDevices);
      return "product-sale";
  }
   @GetMapping("sale-accessory")
   public String getSaleAccessory(Model model,
                                 @RequestParam(name = "pageNo", defaultValue = "0")int pageNo,
                                 @RequestParam(name = "searchacc",defaultValue = "")String searchacc,
                                 @RequestParam(name = "type-accessory", defaultValue = "0") int typeAccessory,
                                 @RequestParam(name = "sortacc",defaultValue = "desc")String sortacc)
   {
      model.addAttribute("typeaccessorycheck", typeAccessory);
      model.addAttribute("searchacc", searchacc);
      model.addAttribute("sortacc",sortacc);

      List<AccessoryCategory> accessory = accessoryCategoryService.getAllAccessoryCategories();
      model.addAttribute("accessory", accessory );
      Page<AccessorySaleDTO> saleAccessory = accessoryService.getSaleAccessory(pageNo,searchacc,typeAccessory,sortacc);
      model.addAttribute("currentPage", pageNo);
      model.addAttribute("totalPage", saleAccessory.getTotalPages());


      model.addAttribute("saleAccessory", saleAccessory);
      return "sale-accessory";
   }
}

package com.neteye.controllers;

import com.neteye.persistence.dto.DeviceDto;
import com.neteye.persistence.entities.Device;
import com.neteye.services.DeviceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/device")
public class DeviceController {
    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping("/getDeviceInfo")
    public DeviceDto getDeviceInfo(@RequestParam("ip") String ip) {
        return deviceService.findDeviceByIp(ip);
    }

    @GetMapping("/find")
    public List<Device> findDevices(@RequestParam("conditions") String searchConditions, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Device> devicePage = deviceService.searchDevices(searchConditions, pageable);
        return devicePage.getContent();
    }

}

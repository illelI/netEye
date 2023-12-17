package com.neteye.controllers;

import com.neteye.persistence.dto.DeviceDto;
import com.neteye.services.DeviceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}

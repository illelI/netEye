package com.neteye.controllers;

import com.neteye.components.DeviceSearcher;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.neteye.NetEyeApplication.logger;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    final DeviceSearcher deviceSearcher;

    @Autowired
    public AdminController(DeviceSearcher deviceSearcher) {
        this.deviceSearcher = deviceSearcher;
    }

    @PostMapping(value = "/scan", consumes = "application/json")
    public void startScanning(@RequestBody @Nullable Map<String, String> ip) {
        logger.info(ip.get("startingIP"));
        if(ip.get("startingIP") != null) {
            deviceSearcher.search(ip.get("startingIP"), ip.get("endingIP"));
        }
        else
            deviceSearcher.search();
    }
}

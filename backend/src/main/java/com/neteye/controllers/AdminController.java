package com.neteye.controllers;

import com.neteye.components.DeviceSearcher;
import jakarta.annotation.Nullable;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;



@RestController
@RequestMapping("/admin")
@Log4j2
public class AdminController {
    final DeviceSearcher deviceSearcher;

    @Autowired
    public AdminController(DeviceSearcher deviceSearcher) {
        this.deviceSearcher = deviceSearcher;
    }

    @PostMapping(value = "/scan", consumes = "application/json")
    public void startScanning(@RequestBody @Nullable Map<String, String> searchProperties) throws InterruptedException {
        log.info("Scan requested for ip in range between {} and {}.", searchProperties.get("startingIP"), searchProperties.get("endingIP"));
        deviceSearcher.search(searchProperties);
    }
}

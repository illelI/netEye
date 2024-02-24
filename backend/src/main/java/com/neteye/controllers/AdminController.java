package com.neteye.controllers;

import com.neteye.components.DeviceSearcher;
import com.neteye.persistence.entities.IpBlackList;
import com.neteye.persistence.repositories.DeviceRepository;
import com.neteye.persistence.repositories.IpBlackListRepository;
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
    final IpBlackListRepository blackListRepository;
    final DeviceRepository deviceRepository;

    @Autowired
    public AdminController(DeviceSearcher deviceSearcher, IpBlackListRepository blackListRepository, DeviceRepository deviceRepository) {
        this.deviceSearcher = deviceSearcher;
        this.blackListRepository = blackListRepository;
        this.deviceRepository = deviceRepository;
    }

    @PostMapping(value = "/scan", consumes = "application/json")
    public void startScanning(@RequestBody @Nullable Map<String, String> searchProperties) throws InterruptedException {
        log.info("Scan requested for ip in range between {} and {}.", searchProperties.get("startingIP"), searchProperties.get("endingIP"));
        deviceSearcher.prepareToScan(searchProperties);
    }

    @PostMapping(value = "/addToBlacklist")
    public void addToBlacklist(@RequestBody String ip) {
        blackListRepository.save(new IpBlackList(ip));
        log.info("Added ip {} to blacklist", ip);
    }

    @PostMapping(value = "/deleteFromDatabase")
    public void deleteIpFromDatabase(@RequestBody String ip) {
        deviceRepository.deleteById(ip);
        log.info("Successfully deleted {} from database", ip);
    }
}

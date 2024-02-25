package com.neteye.controllers;

import com.neteye.components.DeviceSearcher;
import com.neteye.persistence.dto.EmailDto;
import com.neteye.persistence.dto.UserInfoDto;
import com.neteye.persistence.entities.IpBlackList;
import com.neteye.persistence.repositories.DeviceRepository;
import com.neteye.persistence.repositories.IpBlackListRepository;
import com.neteye.services.DeviceService;
import com.neteye.services.UserService;
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
    final DeviceService deviceService;
    final UserService userService;

    @Autowired
    public AdminController(DeviceSearcher deviceSearcher, IpBlackListRepository blackListRepository, DeviceService deviceService, UserService userService) {
        this.deviceSearcher = deviceSearcher;
        this.blackListRepository = blackListRepository;
        this.deviceService = deviceService;
        this.userService = userService;
    }

    @PostMapping(value = "/scan", consumes = "application/json")
    public void startScanning(@RequestBody @Nullable Map<String, String> searchProperties) throws InterruptedException {
        log.info("Scan requested for ip in range between {} and {}.", searchProperties.get("startingIP"), searchProperties.get("endingIP"));
        deviceSearcher.prepareToScan(searchProperties);
    }

    @PostMapping("/delete")
    public void deleteDevice(@RequestBody IpBlackList ip) {
        deviceService.delete(ip.getIp());
    }

    @PostMapping("/addToBlacklist")
    public void addToBlacklist(@RequestBody IpBlackList ip) {
        deviceService.addToBlacklist(ip.getIp());
    }

    @PostMapping("/deleteFromBlacklist")
    public void deleteFromBlacklist(@RequestBody IpBlackList ip) {
        deviceService.deleteFromBlacklist(ip.getIp());
    }

    @PostMapping("/findUser")
    public UserInfoDto findUser(@RequestBody EmailDto email) {
        return userService.findUser(email.getEmail());
    }

    @PostMapping("/changeAccType")
    public void changeAccType(@RequestBody EmailDto emailDto) {
        userService.changeAccountType(emailDto.getEmail());
    }

    @PostMapping("/deleteAcc")
    public void deleteAcc(@RequestBody EmailDto emailDto) {
        userService.deleteUser(emailDto.getEmail());
    }

}

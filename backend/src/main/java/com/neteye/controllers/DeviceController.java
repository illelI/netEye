package com.neteye.controllers;

import com.neteye.persistence.dto.DeviceDto;
import com.neteye.persistence.entities.Device;
import com.neteye.persistence.entities.IpBlackList;
import com.neteye.persistence.repositories.IpBlackListRepository;
import com.neteye.persistence.repositories.PortInfoRepository;
import com.neteye.services.DeviceService;
import com.neteye.utils.mappers.DeviceMapper;
import com.neteye.utils.misc.SearchResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

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
    public SearchResult findDevices(@RequestParam("criteria") String searchConditions, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "15") int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Device> devicePage = deviceService.searchDevices(searchConditions, pageable);
        SearchResult searchResult = new SearchResult();
        searchResult.setNumberOfPages(devicePage.getTotalPages());
        searchResult.setNumberOfFoundDevices(devicePage.getTotalElements());
        searchResult.setQuery(searchConditions);
        searchResult.setCurrentPage(devicePage.getNumber());
        searchResult.setDevices(
                devicePage.getContent().stream()
                    .map(DeviceMapper::toDto)
                    .toList()
        );
        return searchResult;
    }


}

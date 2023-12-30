package com.neteye.utils.mappers;

import com.neteye.persistence.dto.PortInfoDto;
import com.neteye.persistence.entities.PortInfo.PortInfo;

import java.util.ArrayList;
import java.util.List;

public class PortInfoMapper {
    public static List<PortInfoDto> toDto(List<PortInfo> infos) {
        List<PortInfoDto> portInfos = new ArrayList<>();

        for (PortInfo p : infos) {
            PortInfoDto dto = new PortInfoDto();
            dto.setPort(p.getPrimaryKey().getPort());
            dto.setInfo(p.getInfo());
            dto.setAppName(p.getAppName());
            dto.setAppVersion(p.getAppVersion());
            portInfos.add(dto);
        }

        return portInfos;
    }
}

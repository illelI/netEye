package com.neteye.utils.misc;

import com.neteye.persistence.dto.DeviceDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SearchResult {
    int numberOfPages;
    long numberOfFoundDevices;
    int currentPage;
    String query;
    List<DeviceDto> devices;
}

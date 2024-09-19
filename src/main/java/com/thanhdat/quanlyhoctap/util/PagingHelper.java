package com.thanhdat.quanlyhoctap.util;


import com.thanhdat.quanlyhoctap.config.PaginationProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PagingHelper {
    PaginationProperties paginationProperties;

    public Pageable getPageable(Map<String, String> params) {
        Integer page = 0;
        Integer pageSize = paginationProperties.getPageSize();

        if (params.containsKey("page"))
            page = Integer.parseInt(params.get("page")) - 1;

        return PageRequest.of(page, pageSize);
    }
}

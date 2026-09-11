package com.logikaintermedia.erp.utility;

import java.util.List;

public record DataTableResponse<T>(int draw,
        long recordsTotal,
        long recordsFiltered,
        List<T> data) {
}
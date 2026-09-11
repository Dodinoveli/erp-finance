package com.logikaintermedia.erp.utility;

public record DataCountResponse<T>(
        int total,
        int totalActive,
        int totalInactive,
        int totalNew) {

}

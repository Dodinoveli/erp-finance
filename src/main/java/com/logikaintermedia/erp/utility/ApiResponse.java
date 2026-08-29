package com.logikaintermedia.erp.utility;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String status;
    private int code;
    private String message;
    private String timestamp;
    private T data;
    private T errors;
    private Integer count;

    // ================= SUCCESS =================

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .timestamp(now())
                .build();
    }

    public static <T> ApiResponse<T> created(String message, T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(HttpStatus.CREATED.value())
                .message(message)
                .data(data)
                .timestamp(now())
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data, int count) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message(message)
                .data(data)
                .count(count)
                .timestamp(now())
                .build();
    }

    // ================= ERROR =================

    public static <T> ApiResponse<T> error(HttpStatus status, String message) {
        return ApiResponse.<T>builder()
                .status("error")
                .code(status.value())
                .message(message)
                .timestamp(now())
                .build();
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String message, T errors) {
        return ApiResponse.<T>builder()
                .status("error")
                .code(status.value())
                .message(message)
                .errors(errors)
                .timestamp(now())
                .build();
    }

    // ================= UTIL =================

    private static String now() {
        return ZonedDateTime.now()
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }
}

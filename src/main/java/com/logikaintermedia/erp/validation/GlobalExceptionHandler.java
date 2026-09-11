package com.logikaintermedia.erp.validation;

import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.logikaintermedia.erp.utility.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {
        private ErrorConfig errorConfig;

        //Handler spesifik untuk IllegalArgumentException
        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<ApiResponse<?>> handleIllegalArgument(IllegalArgumentException ex) {
                log.warn("IllegalArgument: {}", ex.getMessage());
                
                return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage()  // Pesan error Anda
                ));
        }

        // =========================
        // 1. VALIDATION ERROR (@Valid)
        // =========================
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
                return ResponseEntity
                                .badRequest()
                                .body(ApiResponse.error(
                                                HttpStatus.BAD_REQUEST,
                                                "Data tidak valid. Silakan periksa kembali input Anda.",
                                                errors));
        }

        // 2. DATABASE UNIQUE CONSTRAINT (Versi Baru pakai YAML)
        @SuppressWarnings("null")
        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ApiResponse<?>> handleDataIntegrity(DataIntegrityViolationException ex) {
                String detail = ex.getMostSpecificCause().getMessage();
                log.warn("Database Constraint Violated: {}", detail);

                String msg = errorConfig.getDatabase().getUniqueConstraints().entrySet().stream()
                                .filter(entry -> detail.contains(entry.getKey()))
                                .map(Map.Entry::getValue)
                                .findFirst()
                                .orElse(errorConfig.getDatabase().getDefaultUnique());

                return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body(ApiResponse.error(HttpStatus.CONFLICT,
                                                msg));
        }

        // =========================
        // 3. BUSINESS ERROR (throw manual)
        // =========================
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<?> handleRuntime(RuntimeException ex) {
                return ResponseEntity.badRequest().body(ApiResponse.error(HttpStatus.BAD_REQUEST, ex.getMessage()));
        }

        // =========================
        // 4. GENERAL ERROR (SYSTEM ERROR)
        // =========================
        @ExceptionHandler(Exception.class)
        public ResponseEntity<?> handleException(Exception ex) {
                ex.printStackTrace(); // log untuk debug

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "Terjadi kesalahan sistem"));
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ApiResponse<?>> handleHttpMessageNotReadable(
                        HttpMessageNotReadableException ex) {

                return ResponseEntity.badRequest().body(
                                ApiResponse.builder()
                                                .message("Format angka yang anda masukan tidak valid")
                                                .build());
        }
}

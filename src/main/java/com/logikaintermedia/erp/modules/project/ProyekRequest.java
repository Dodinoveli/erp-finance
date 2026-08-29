package com.logikaintermedia.erp.modules.project;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.logikaintermedia.erp.validation.OnCreate;
import com.logikaintermedia.erp.validation.OnUpdate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProyekRequest {

    private UUID projectId;

    @NotBlank(message = "No PO/ SPK wajib di isi", groups = { OnCreate.class, OnUpdate.class })
    private String projectCode;

    @NotBlank(message = "Judul Proyek wajib di isi", groups = { OnCreate.class, OnUpdate.class })
    private String name;

    @NotBlank(message = "Deskripsi wajib di isi", groups = { OnCreate.class, OnUpdate.class })
    private String description;

    @NotNull(message = "Klien/ Pelanggan wajib di isi", groups = { OnCreate.class, OnUpdate.class })
    private UUID clientId;

    @NotBlank(message = "Jenis Proyek wajib di isi", groups = { OnCreate.class, OnUpdate.class })
    private String projectType;

    @NotNull(message = "Nilai Kontrak wajib diisi", groups = { OnCreate.class,
            OnUpdate.class })
    @DecimalMin(value = "0.01", message = "Nilai Kontrok harus lebih dari 0", groups = {
            OnCreate.class,
            OnUpdate.class })
    private BigDecimal contractValue;

    private BigDecimal dpp;

    @NotBlank(message = "Lokasi wajib di isi", groups = { OnCreate.class, OnUpdate.class })
    private String location;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime createdAt = LocalDateTime.now();

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime updatedAt = LocalDateTime.now();
    private UUID companyId;
    private UUID userId;

    @NotBlank(message = "Skema PPN wajib di isi", groups = { OnCreate.class, OnUpdate.class })
    private String taxType;

    private BigDecimal vatRate;

    @NotNull(message = "PO Date wajib diisi", groups = { OnCreate.class, OnUpdate.class })
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate poDate;

    private BigDecimal totallTax;
    private BigDecimal totalAmount;

    // detail
    // private UUID detailId;
    // // private UUID projectId;
    // private String itemName;
    // private String descriptiond;
    // private BigDecimal volume;
    // private String unit;
    // private BigDecimal unitPrice;
    // private BigDecimal discountPercent;
    // private BigDecimal discountAmount;
    // private BigDecimal totalPrice;
}

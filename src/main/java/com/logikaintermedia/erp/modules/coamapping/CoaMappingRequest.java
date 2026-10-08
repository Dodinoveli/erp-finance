package com.logikaintermedia.erp.modules.coamapping;

import com.logikaintermedia.erp.validation.OnCreate;
import com.logikaintermedia.erp.validation.OnUpdate;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CoaMappingRequest {

	@NotBlank(message = "Jenis Transaksi wajib diisi", groups = { OnCreate.class })
	private String transactionType;

	@NotBlank(message = "Jenis Pembayaran wajib diisi", groups = { OnCreate.class })
	private String paymentType;

	@NotBlank(message = "Keterangan wajib diisi", groups = { OnCreate.class, OnUpdate.class })
	private String description;

}

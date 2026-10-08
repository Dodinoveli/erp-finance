package com.logikaintermedia.erp.modules.coamapping;

public enum TransactionType {
	PROJECT_DP, PROJECT_PROGRESS, PURCHASE_MATERIAL, EXPENSE, RETENTION;

	public boolean isPaymentTypeAllowed(PaymentType paymentType) {
		return switch (this) {
		case PROJECT_DP, PROJECT_PROGRESS, RETENTION ->
			paymentType == PaymentType.CASH || paymentType == PaymentType.BANK;
		case PURCHASE_MATERIAL, EXPENSE -> true;
		};

	}

}

/**
 * PROJECT_DP = Uang muka proyek PROJECT_PROGRESS = Pembayaran progres/termin
 * proyek PURCHASE_MATERIAL = Pembelian material proyek EXPENSE =
 * Pengeluaran/beban operasional RETENTION = Penerimaan uang retensi
 */
/**
 * 1. PROJECT_DP Penerimaan uang muka proyek 2. PROJECT_PROGRESS Penerimaan
 * pembayaran berdasarkan progres/termin 3. RETENTION Penerimaan pembayaran
 * retensi 4. PURCHASE_MATERIAL Pembelian material proyek 5. EXPENSE
 * Pengeluaran/beban operasional
 * 
 * Tapi nanti bisa berkembang, misalnya: 6. PROJECT_REFUND Pengembalian uang
 * kepada customer terkait proyek
 * 
 * 7. CUSTOMER_REFUND Pengembalian kelebihan pembayaran customer
 * 
 * 8. SUPPLIER_PAYMENT Pembayaran hutang kepada supplier
 * 
 * 9. EMPLOYEE_PAYMENT Pembayaran gaji/upah atau kewajiban kepada pekerja
 * 
 * 10. TAX_PAYMENT Pembayaran kewajiban pajak
 */
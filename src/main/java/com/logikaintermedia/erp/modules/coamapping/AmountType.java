package com.logikaintermedia.erp.modules.coamapping;
/**
 * AmountType adalah enum yang digunakan untuk menentukan jenis nominal
 * transaksi yang digunakan pada setiap baris COA Mapping saat sistem
 * membentuk jurnal akuntansi.
 *
 * <p>Jenis nominal yang tersedia:</p>
 *
 * <pre>
 * AmountType   | Kegunaan
 * -------------|------------------------------------------------
 * SUBTOTAL     | Mengambil nilai sebelum pajak
 * TAX          | Mengambil nilai pajak
 * TOTAL        | Mengambil nilai akhir transaksi setelah pajak
 * </pre>
 */
public enum AmountType {
	SUBTOTAL, 
	TAX, 
	TOTAL
}

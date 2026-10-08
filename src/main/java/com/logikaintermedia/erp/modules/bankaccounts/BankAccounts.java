package com.logikaintermedia.erp.modules.bankaccounts;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import com.fasterxml.uuid.Generators;
import com.logikaintermedia.erp.modules.chartofaccounts.ChartOfAccounts;
import lombok.Data;

@Data
public class BankAccounts {
	private UUID bankAccountId;
	private String accountCode;
	private String accountName;
	private String bankName;
	private String bankBranch;
	private String accountNumber;
	private String accountHolder;
	private String currency = "IDR";
	private BigDecimal openingBalance = BigDecimal.ZERO;
	private Boolean isDefault = false;
	private Boolean isActive = true;
	private UUID companyId;

	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	private UUID userId;
	private OffsetDateTime deletedAt;

	// relasi ke tabel joa
	private UUID accountId;
	// hasil json
	private ChartOfAccounts coa;

	private String parentAccountCode;
	private String parentAccountName;
	private String childAccountCode;
	private String childAccountName;

	/**
	 * Membuat BankAccounts baru dari BankAccountsRequest.
	 * 
	 * @param request   data BankAccounts
	 * @param code      kode BankAccounts
	 * @param companyId ID perusahaan
	 * @param userId    ID user pembuat data
	 * @return BankAccounts baru
	 */

	public static BankAccounts from(BankAccountsRequest request, UUID bankAccountId, UUID companyId, UUID userId) {
		BankAccounts accounts = new BankAccounts();
		accounts.setBankAccountId(Generators.timeBasedEpochRandomGenerator().generate());
		accounts.setAccountCode(request.getAccountCode());
		accounts.setAccountName(request.getAccountName());
		accounts.setBankName(request.getBankName());
		accounts.setBankBranch(request.getBankBranch());
		accounts.setAccountNumber(request.getAccountNumber());
		accounts.setAccountHolder(request.getAccountHolder());
		accounts.setCurrency(request.getCurrency());
		accounts.setOpeningBalance(request.getOpeningBalance());
		accounts.setIsDefault(request.getIsDefault());
		accounts.setIsActive(request.getIsActive());
		accounts.setCompanyId(companyId);
		accounts.setAccountId(request.getAccountId());
		accounts.setCreatedAt(request.getCreatedAt());
		accounts.setUpdatedAt(null);
		accounts.setUserId(userId);
		accounts.setDeletedAt(null);
		return accounts;
	}

	/**
	 * Membuat perubahan data BankAccounts berdasarkan ClientRequest.
	 *
	 * @param dto           data BankAccounts yang diperbarui
	 * @param bankAccountId ID BankAccounts yang diperbarui
	 * @param companyId     ID perusahaan pemilik data
	 * @return BankAccounts dengan data yang telah diperbarui
	 */
	public static BankAccounts updateFrom(BankAccountsRequest request, UUID bankAccountId, UUID companyId,
			UUID userId) {
		BankAccounts accounts = new BankAccounts();
		accounts.setAccountCode(request.getAccountCode());
		accounts.setAccountName(request.getAccountName());
		accounts.setBankName(request.getBankName());
		accounts.setBankBranch(request.getBankBranch());
		accounts.setAccountNumber(request.getAccountNumber());
		accounts.setAccountHolder(request.getAccountHolder());
		accounts.setOpeningBalance(BigDecimal.ZERO);
		accounts.setIsActive(true);
		accounts.setAccountId(request.getAccountId());
		accounts.setUpdatedAt(request.getUpdatedAt());
		accounts.setBankAccountId(bankAccountId);
		accounts.setCompanyId(companyId);
		accounts.setUserId(userId);
		return accounts;
	}

}

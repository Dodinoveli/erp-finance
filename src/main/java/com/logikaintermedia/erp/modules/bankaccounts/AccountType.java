package com.logikaintermedia.erp.modules.bankaccounts;

public enum AccountType {
    CASH,
    BANK, EWALLET;

    // @JsonCreator
    // public static AccountType from(String value) {
    // return AccountType.valueOf(value.toUpperCase());
    // }
}

package com.pay.opay;

public class SearchBankItem {
    private String bankName;
    private int bankImage;
    private String bankCode;

    public SearchBankItem(String bankName, int bankImage, String bankCode) {
        this.bankName = bankName;
        this.bankImage = bankImage;
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public int getBankImage() {
        return bankImage;
    }

    public String getBankCode() {
        return bankCode;
    }
}

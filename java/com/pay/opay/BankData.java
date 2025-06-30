package com.pay.opay;

public class BankData {
    private String bankCode;
    private String bankName;
    private int bankImage;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String code) {
        bankCode = code;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String name) {
        bankName = name;
    }

    public String getBankData() {
        return bankCode + " " + bankName;
    }

    public int getBankImage() {
        return bankImage;
    }

    public void setBankImage(int bankImage) {
        this.bankImage = bankImage;
    }
}

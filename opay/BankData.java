package com.pay.opay;

public class BankData {
    private static final BankData instance = new BankData();  // Singleton instance

    private String bankCode;
    private String bankName;
    private int bankImage;

    // Private constructor to prevent instantiation
    private BankData() {}

    // Public method to access the singleton instance
    public static BankData getInstance() {
        return instance;
    }

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

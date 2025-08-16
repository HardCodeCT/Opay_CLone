package com.pay.opay;

public class BankItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_BANK = 1;
    private int type;
    private String name;
    private int logoResId;
    private String bankCode;
    public BankItem(String name) {
        this.type = TYPE_HEADER;
        this.name = name;
        this.logoResId = -1;
        this.bankCode = null;
    }

    // Constructor for bank
    public BankItem(String name, int logoResId, String bankCode) {
        this.type = TYPE_BANK;
        this.name = name;
        this.logoResId = logoResId;
        this.bankCode = bankCode;
    }

    public int getType() { return type; }
    public String getName() { return name; }
    public int getLogoResId() { return logoResId; }
    public String getBankCode() { return bankCode; }
}

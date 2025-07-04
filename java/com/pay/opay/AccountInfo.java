package com.pay.opay;

public class AccountInfo {
    private static final AccountInfo instance = new AccountInfo(); // Singleton instance

    private String amount;
    private String userAccount;
    private String userNumber;
    private String rootAccount;
    private String rootNumber;
    private String userBank;
    private String rootBank;
    private int Response;
    private int bankUpdate;

    // Private constructor to prevent others from creating new instances
    private AccountInfo() {}

    public static AccountInfo getInstance() {
        return instance;
    }

    public void reset() {
        amount = null;
        userAccount = null;
        userNumber = null;
        rootAccount = null;
        rootNumber = null;
        userBank = null;
        rootBank = null;
        Response = 0;
        bankUpdate = 0;
    }

    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getUserAccount() {
        return userAccount;
    }
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }
    public String getUserNumber() {
        return userNumber;
    }
    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }
    public String getRootAccount() {
        return rootAccount;
    }
    public void setRootAccount(String rootAccount) {
        this.rootAccount = rootAccount;
    }
    public String getRootNumber() {
        return rootNumber;
    }
    public void setRootNumber(String rootNumber) {
        this.rootNumber = rootNumber;
    }
    public String getUserBank() {
        return userBank;
    }
    public void setUserBank(String userBank) {
        this.userBank = userBank;
    }
    public String getRootBank() {
        return rootBank;
    }
    public void setRootBank(String rootBank) {
        this.rootBank = rootBank;
    }
    public int getResponse() {
        return Response;
    }
    public void setResponse(int response) {
        this.Response = response;
    }
    public int getBankUpdate() {
        return bankUpdate;
    }
    public void setBankUpdate(int bankUpdate) {
        this.bankUpdate = bankUpdate;
    }
}

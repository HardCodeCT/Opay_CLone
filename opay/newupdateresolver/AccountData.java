package com.pay.opay.newupdateresolver;

import com.google.gson.annotations.SerializedName;

public class AccountData {
    @SerializedName("account_number")
    private String accountNumber;

    @SerializedName("account_name")
    private String accountName;

    // Default constructor
    public AccountData() {
    }

    // Parameterized constructor
    public AccountData(String accountNumber, String accountName) {
        this.accountNumber = accountNumber;
        this.accountName = accountName;
    }

    // GETTERS
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    // SETTERS
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "AccountData{" +
                "accountNumber='" + accountNumber + '\'' +
                ", accountName='" + accountName + '\'' +
                '}';
    }
}

package com.pay.opay.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BankName {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String accountName;
    private String bankName;
    private String bankNumber;
    private int imageName;

    // Getters
    public int getId() {
        return id;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public int getImageName() {
        return imageName;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public void setImageName(int imageName) {
        this.imageName = imageName;
    }
}

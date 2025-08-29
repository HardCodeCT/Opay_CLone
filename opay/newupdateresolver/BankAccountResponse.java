package com.pay.opay.newupdateresolver;

import com.google.gson.annotations.SerializedName;

public class BankAccountResponse {
    @SerializedName("status")
    private boolean status;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private AccountData data;

    // Default constructor
    public BankAccountResponse() {
    }

    // Parameterized constructor
    public BankAccountResponse(boolean status, String message, AccountData data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // GETTERS
    public boolean isStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public AccountData getData() {
        return data;
    }

    // SETTERS
    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(AccountData data) {
        this.data = data;
    }

    // toString method for debugging
    @Override
    public String toString() {
        return "BankAccountResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
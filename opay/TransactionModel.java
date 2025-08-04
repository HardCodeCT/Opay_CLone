package com.pay.opay;

public class TransactionModel {
    private String user, dateTime, amount, status;

    public TransactionModel(String user, String dateTime, String amount, String status) {
        this.user = user;
        this.dateTime = dateTime;
        this.amount = amount;
        this.status = status;
    }

    public String getUser() { return user; }
    public String getDateTime() { return dateTime; }
    public String getAmount() { return amount; }
    public String getStatus() { return status; }
}


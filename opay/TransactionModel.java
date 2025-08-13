package com.pay.opay;

public class TransactionModel {
    private String user, dateTime, amount, status;
    private int number;

    public TransactionModel(int number,String user, String dateTime, String amount, String status) {
        this.number = number;
        this.user = user;
        this.dateTime = dateTime;
        this.amount = amount;
        this.status = status;
    }

    public int getID(){return number;}
    public String getUser() { return user; }
    public String getDateTime() { return dateTime; }
    public String getAmount() { return amount; }
    public String getStatus() { return status; }

}


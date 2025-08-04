package com.pay.opay.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BankTransfer {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String senderName;
    public String accountNumber;
    public String bankName;
    public String amount;
    public  String longdatetime;
    public  String shortdatetime;
    public long timestamp;
    public String imageName;
}

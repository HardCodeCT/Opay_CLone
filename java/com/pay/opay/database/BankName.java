package com.pay.opay.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BankName {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String accountName;
    public String bankName;
    public  String bankNumber;
    public int imageName;
}

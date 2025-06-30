package com.pay.opay.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "amount_table")
public class Amount {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private int amountValue;
    private String description;
    private long timestamp;

    public Amount(int amountValue, String description) {
        this.amountValue = amountValue;
        this.description = description;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmountValue() {
        return amountValue;
    }

    public String getDescription() {
        return description;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
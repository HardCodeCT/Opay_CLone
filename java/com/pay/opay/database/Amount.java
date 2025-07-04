package com.pay.opay.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "amount_table")
public class Amount {
    @PrimaryKey
    private int id = 1; // Fixed ID to ensure only one row

    private int amountValue;
    private boolean stateValue = false; // Default to false
    private long timestamp = System.currentTimeMillis();

    // Empty constructor for Room
    public Amount() {}

    public Amount(int amountValue) {
        this.amountValue = amountValue;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getAmountValue() {
        return amountValue;
    }

    public boolean getStateValue() {
        return stateValue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setAmountValue(int amountValue) {
        this.amountValue = amountValue;
    }

    public void setStateValue(boolean stateValue) {
        this.stateValue = stateValue;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Helper method to check state and perform action
    public void checkStateAndPerform(Runnable actionIfFalse) {
        if (!stateValue) {
            actionIfFalse.run();
        }
    }
}

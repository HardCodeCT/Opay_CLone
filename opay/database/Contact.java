package com.pay.opay.database;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Contact {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String phone;
    public int imageResId;

    public Contact(String name, String phone, int imageResId) {
        this.name = name;
        this.phone = phone;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getImageId() {
        return imageResId;
    }
}

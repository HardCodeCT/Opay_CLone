package com.pay.opay.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {

    @Insert
    void insert(Contact contact);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);

    @Query("SELECT * FROM Contact ORDER BY id DESC")  // Optional: shows newest first
    LiveData<List<Contact>> getAllContacts();

    @Query("SELECT COUNT(*) FROM Contact WHERE name = :name AND phone = :phone")
    int countByNameAndPhone(String name, String phone);
}

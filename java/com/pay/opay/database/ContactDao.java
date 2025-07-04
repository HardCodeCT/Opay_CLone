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

    // Search for partial matches by phone or name
    @Query("SELECT * FROM Contact WHERE phone LIKE :query OR name LIKE :query ORDER BY id DESC")
    LiveData<List<Contact>> findMatchingContacts(String query);

    // Search for exact phone match (used when input is exactly 10 digits)
    @Query("SELECT * FROM Contact WHERE phone = :phone LIMIT 1")
    LiveData<List<Contact>> findExactContactByPhone(String phone);

}


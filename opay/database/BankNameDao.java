package com.pay.opay.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BankNameDao {

    @Insert
    void insert(BankName bankName);

    @Update
    void update(BankName bankName);

    @Delete
    void delete(BankName bankName);

    @Query("SELECT * FROM BankName ORDER BY bankName ASC")
    LiveData<List<BankName>> getAllBanks();

    @Query("SELECT * FROM BankName WHERE bankName = :name LIMIT 1")
    BankName getBankByName(String name);

    @Query("SELECT COUNT(*) FROM BankName WHERE bankName = :name")
    int countByName(String name);

    @Query("SELECT * FROM BankName WHERE bankNumber LIKE :query OR accountName LIKE :query ORDER BY id DESC")
    LiveData<List<BankName>> findMatchingContacts(String query);

    @Query("SELECT * FROM BankName WHERE bankNumber = :phone LIMIT 1")
    LiveData<List<BankName>> findExactContactByPhone(String phone);


}
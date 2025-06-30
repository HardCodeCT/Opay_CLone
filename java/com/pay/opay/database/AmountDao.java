package com.pay.opay.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AmountDao {
    @Insert
    void insert(Amount amount);

    @Update
    void update(Amount amount);

    @Delete
    void delete(Amount amount);

    @Query("DELETE FROM amount_table")
    void deleteAllAmounts();

    @Query("SELECT * FROM amount_table ORDER BY timestamp DESC")
    LiveData<List<Amount>> getAllAmounts();

    @Query("SELECT SUM(amountValue) FROM amount_table")
    LiveData<Integer> getTotalAmount();

    @Query("SELECT * FROM amount_table WHERE amountValue > :minValue")
    LiveData<List<Amount>> getAmountsAbove(int minValue);
}
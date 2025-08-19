package com.pay.opay.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AmountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdate(Amount amount);

    @Update
    void update(Amount amount);

    @Query("SELECT * FROM amount_table WHERE id = 1 LIMIT 1")
    LiveData<Amount> getAmount();

    @Query("SELECT stateValue FROM amount_table WHERE id = 1 LIMIT 1")
    LiveData<Boolean> getStateValue();

    @Query("UPDATE amount_table SET stateValue = :newState WHERE id = 1")
    void updateState(boolean newState);

    @Query("SELECT COUNT(*) FROM amount_table")
    int count();

    @Query("SELECT amountValue FROM amount_table WHERE id = 1")
    LiveData<Integer> getAmountValue();

    @Query("SELECT amountValue FROM amount_table WHERE id = 1 LIMIT 1")
    Integer getAmountValueSynchronous();

    @Query("SELECT * FROM amount_table WHERE id = 1 LIMIT 1")
    Amount getAmountSynchronous();
}
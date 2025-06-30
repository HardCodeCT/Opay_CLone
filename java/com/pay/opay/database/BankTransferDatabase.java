package com.pay.opay.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BankTransfer.class}, version = 1)
public abstract class BankTransferDatabase extends RoomDatabase {

    private static BankTransferDatabase instance;

    public abstract BankTransferDao bankTransferDao();

    public static synchronized BankTransferDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    BankTransferDatabase.class,
                    "bank_transfer_database"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}

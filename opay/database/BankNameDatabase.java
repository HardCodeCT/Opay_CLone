package com.pay.opay.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BankName.class}, version = 1, exportSchema = false)
public abstract class BankNameDatabase extends RoomDatabase {

    private static BankNameDatabase instance;
    public abstract BankNameDao bankNameDao();

    public static synchronized BankNameDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    BankNameDatabase.class,
                    "bank_name_database"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}

package com.pay.opay.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {Amount.class}, version = 1, exportSchema = false)
public abstract class AmountDatabase extends RoomDatabase {
    public abstract AmountDao amountDao();

    private static volatile AmountDatabase INSTANCE;

    public static AmountDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AmountDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AmountDatabase.class, "amount_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
package com.pay.opay.utils;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.function.Consumer;

public class AmountHelper {

    private final DatabaseReference databaseRef;

    // Pass in your databaseRef from the activity
    public AmountHelper(DatabaseReference databaseRef) {
        this.databaseRef = databaseRef;
    }

    public void retrieveAmount(Consumer<Integer> callback) {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String amountStr = snapshot.getValue(String.class);
                    if (amountStr != null) {
                        try {
                            int amount = Integer.parseInt(amountStr);

                            if (amount == 0) {
                                // TODO: handle when amount == 0
                            }

                            callback.accept(amount);
                        } catch (NumberFormatException e) {
                            callback.accept(0);
                        }
                    } else {
                        callback.accept(0);
                    }
                } else {
                    callback.accept(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.accept(0);
            }
        });
    }
}

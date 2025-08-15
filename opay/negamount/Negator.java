package com.pay.opay.negamount;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.pay.opay.Repository.AmountRepository;
import com.pay.opay.database.Amount;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Negator {
    private final AmountRepository repository;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Application application;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public Negator(Application application) {
        repository = new AmountRepository(application);
        this.application = application;
    }

    public void negate(int valueToSubtract) {
        executor.execute(() -> {
            mainHandler.post(() -> Toast.makeText(application, "Entered negate()", Toast.LENGTH_SHORT).show());

            Amount amount = repository.getCurrentAmount().getValue();
            if (amount != null) {
                int currentAmount = amount.getAmountValue();

                mainHandler.post(() -> Toast.makeText(application,
                        "Current amount: " + currentAmount, Toast.LENGTH_LONG).show());

                if (currentAmount < valueToSubtract) {
                    mainHandler.post(() -> Toast.makeText(application, "Amount less", Toast.LENGTH_SHORT).show());
                    //Terminator.killApp();
                }

                int newAmount = currentAmount - valueToSubtract;
                amount.setAmountValue(newAmount);
                repository.insertOrUpdateAmount(amount);
            }else{
                mainHandler.post(() -> Toast.makeText(application, "Amount is null", Toast.LENGTH_SHORT).show());
            }
        });
    }
}
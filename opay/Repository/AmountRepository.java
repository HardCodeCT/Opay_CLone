package com.pay.opay.Repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.pay.opay.database.Amount;
import com.pay.opay.database.AmountDao;
import com.pay.opay.database.AmountDatabase;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AmountRepository {
    private final AmountDao amountDao;
    private final LiveData<Amount> currentAmount;
    private final LiveData<Boolean> currentState;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public AmountRepository(Application application) {
        AmountDatabase db = AmountDatabase.getInstance(application);
        amountDao = db.amountDao();
        currentAmount = amountDao.getAmount();
        currentState = amountDao.getStateValue();

        // Initialize with default amount if empty
        executor.execute(() -> {
            if (amountDao.count() == 0) {
                amountDao.insertOrUpdate(new Amount(0));
            }
        });
    }

    public LiveData<Amount> getCurrentAmount() {
        return currentAmount;
    }

    public LiveData<Boolean> getCurrentState() {
        return currentState;
    }

    public void insertOrUpdateAmount(Amount amount) {
        executor.execute(() -> amountDao.insertOrUpdate(amount));
    }

    public void updateAmountValue(int newValue) {
        executor.execute(() -> {
            Amount amount = amountDao.getAmount().getValue();
            if (amount != null) {
                amount.setAmountValue(newValue);
                amountDao.update(amount);
            }
        });
    }

    public void updateState(boolean newState) {
        executor.execute(() -> amountDao.updateState(newState));
    }

    public void checkStateAndPerform(Runnable actionIfFalse) {
        executor.execute(() -> {
            Amount amount = amountDao.getAmount().getValue();
            if (amount != null && !amount.getStateValue()) {
                actionIfFalse.run();
            }
        });
    }

    public void resetAmount() {
        executor.execute(() -> {
            Amount amount = amountDao.getAmount().getValue();
            if (amount != null) {
                amount.setAmountValue(0);
                amount.setStateValue(false);
                amountDao.update(amount);
            }
        });
    }

    public LiveData<Integer> getAmountValue() {
        return amountDao.getAmountValue();
    }
}
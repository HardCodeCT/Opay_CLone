package com.pay.opay;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.pay.opay.database.Amount;
import com.pay.opay.database.AmountDao;
import com.pay.opay.database.AmountDatabase;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AmountRepository {
    private final AmountDao amountDao;
    private final LiveData<List<Amount>> allAmounts;
    private final LiveData<Integer> totalAmount;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public AmountRepository(Application application) {
        AmountDatabase db = AmountDatabase.getInstance(application);
        amountDao = db.amountDao();
        allAmounts = amountDao.getAllAmounts();
        totalAmount = amountDao.getTotalAmount();
    }

    public LiveData<List<Amount>> getAllAmounts() {
        return allAmounts;
    }

    public LiveData<Integer> getTotalAmount() {
        return totalAmount;
    }

    public void insert(Amount amount) {
        executor.execute(() -> amountDao.insert(amount));
    }

    public void update(Amount amount) {
        executor.execute(() -> amountDao.update(amount));
    }

    public void delete(Amount amount) {
        executor.execute(() -> amountDao.delete(amount));
    }

    public void deleteAll() {
        executor.execute(amountDao::deleteAllAmounts);
    }
}
package com.pay.opay.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pay.opay.AmountRepository;
import com.pay.opay.database.Amount;

import java.util.List;

public class AmountViewModel extends AndroidViewModel {
    private final AmountRepository repository;
    private final LiveData<List<Amount>> allAmounts;
    private final LiveData<Integer> totalAmount;

    public AmountViewModel(Application application) {
        super(application);
        repository = new AmountRepository(application);
        allAmounts = repository.getAllAmounts();
        totalAmount = repository.getTotalAmount();
    }

    public LiveData<List<Amount>> getAllAmounts() {
        return allAmounts;
    }

    public LiveData<Integer> getTotalAmount() {
        return totalAmount;
    }

    public void insert(Amount amount) {
        repository.insert(amount);
    }

    public void update(Amount amount) {
        repository.update(amount);
    }

    public void delete(Amount amount) {
        repository.delete(amount);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
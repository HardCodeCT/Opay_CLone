package com.pay.opay.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.pay.opay.AmountRepository;
import com.pay.opay.database.Amount;

public class AmountViewModel extends AndroidViewModel {
    private final AmountRepository repository;
    private final MutableLiveData<Boolean> shouldTriggerAction = new MutableLiveData<>();

    public AmountViewModel(@NonNull Application application) {
        super(application);
        repository = new AmountRepository(application);

        // Initialize with default amount if needed
        repository.checkStateAndPerform(() -> {
            shouldTriggerAction.postValue(true);
        });
    }

    // Get the current amount LiveData
    public LiveData<Amount> getCurrentAmount() {
        return repository.getCurrentAmount();
    }

    // Get the current state LiveData
    public LiveData<Boolean> getCurrentState() {
        return repository.getCurrentState();
    }

    // Observe when action should be triggered (state is false)
    public LiveData<Boolean> shouldTriggerAction() {
        return shouldTriggerAction;
    }

    // Update the amount value
    public void updateAmount(int newAmount) {
        repository.updateAmountValue(newAmount);
    }

    // Update the state value
    public void setState(boolean newState) {
        repository.updateState(newState);
        if (newState) {
            shouldTriggerAction.postValue(false);
        }
    }

    // Check state and perform action if false
    public void checkStateAndPerform(Runnable action) {
        repository.checkStateAndPerform(action);
    }

    // Reset both amount and state
    public void resetAmount() {
        repository.resetAmount();
        shouldTriggerAction.postValue(true);
    }

    // Get the current amount value (blocking call - use carefully)
    public int getCurrentAmountValue() {
        Amount amount = repository.getCurrentAmount().getValue();
        return amount != null ? amount.getAmountValue() : 0;
    }

    // Get the current state value (blocking call - use carefully)
    public boolean getCurrentStateValue() {
        Boolean state = repository.getCurrentState().getValue();
        return state != null ? state : false;
    }

    public LiveData<Integer> getAmountValue() {
        return repository.getAmountValue();
    }
}
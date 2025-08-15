package com.pay.opay.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.pay.opay.database.BankName;
import com.pay.opay.database.BankNameDao;
import com.pay.opay.database.BankNameDatabase;
import java.util.List;

public class BankContactViewModel extends AndroidViewModel {

    private final BankNameDao bankNameDao;
    private final LiveData<List<BankName>> allBanks;

    public BankContactViewModel(@NonNull Application application) {
        super(application);
        BankNameDatabase db = BankNameDatabase.getInstance(application);
        bankNameDao = db.bankNameDao();
        allBanks = bankNameDao.getAllBanks();
    }

    public LiveData<List<BankName>> getAllBanks() {
        return allBanks;
    }

    public BankName getBankByName(String name) {
        return bankNameDao.getBankByName(name);
    }

    public void insert(BankName bankName) {
        new Thread(() -> bankNameDao.insert(bankName)).start();
    }

    public void delete(BankName bankName) {
        new Thread(() -> bankNameDao.delete(bankName)).start();
    }

    public void update(BankName bankName) {
        new Thread(() -> bankNameDao.update(bankName)).start();
    }

    public void insertIfNotExists(BankName bankName) {
        new Thread(() -> {
            BankName existing = bankNameDao.getBankByName(bankName.getBankName()); // Direct field access
            if (existing == null) {
                bankNameDao.insert(bankName);
            }
        }).start();
    }

    public LiveData<List<BankName>> getContactsMatching(String query) {
        return bankNameDao.findMatchingContacts("%" + query + "%");
    }

    public LiveData<List<BankName>> getContactByPhone(String phone) {
        return bankNameDao.findExactContactByPhone(phone);
    }
}
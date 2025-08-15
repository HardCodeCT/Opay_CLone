package com.pay.opay.viewmodel;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pay.opay.LastTwoTransfersWrapper;
import com.pay.opay.database.BankTransfer;
import com.pay.opay.database.BankTransferDao;
import com.pay.opay.database.BankTransferDatabase;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankTransferViewModel extends AndroidViewModel {

    private final BankTransferDao dao;
    private final LiveData<List<BankTransfer>> allTransfers;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public BankTransferViewModel(@NonNull Application application) {
        super(application);
        BankTransferDatabase db = BankTransferDatabase.getInstance(application);
        dao = db.bankTransferDao();
        allTransfers = dao.getAllTransfers();
    }

    public LiveData<List<BankTransfer>> getAllTransfers() {
        return allTransfers;
    }

    public void insert(BankTransfer transfer) {
        executorService.execute(() -> dao.insert(transfer));
    }

    public void update(BankTransfer transfer) {
        executorService.execute(() -> dao.update(transfer));
    }

    public void delete(BankTransfer transfer) {
        executorService.execute(() -> dao.delete(transfer));
    }

    public void countByDetails(String sender, String account, CountCallback callback) {
        executorService.execute(() -> {
            int count = dao.countByDetails(sender, account);
            callback.onCountResult(count);
        });
    }

    public interface CountCallback {
        void onCountResult(int count);
    }

    public interface LastTwoCallback {
        void onResult(LastTwoTransfersWrapper result);
        void onNoEntries(); // Called if 0 entries
    }

    public void getLastTwoTransfersIfExists(LastTwoCallback callback) {
        executorService.execute(() -> {
            List<BankTransfer> list = dao.getLastTwoTransfers();
            if (list.size() >= 2) {
                callback.onResult(new LastTwoTransfersWrapper(list.get(0), list.get(1)));
            } else if (list.size() == 1) {
                callback.onResult(new LastTwoTransfersWrapper(list.get(0), null));
            } else {
                callback.onNoEntries();
            }
        });
    }

    public interface NewTransferCallback {
        void onNewTransfer(BankTransfer transfer);
        void onNoNewTransfer();
    }

    private BankTransfer lastKnownTransfer = null;

    public void checkForNewTransfer(NewTransferCallback callback) {
        executorService.execute(() -> {
            BankTransfer latest = dao.getLatestTransfer();
            if (latest != null && (lastKnownTransfer == null || latest.timestamp > lastKnownTransfer.timestamp)) {
                lastKnownTransfer = latest;
                callback.onNewTransfer(latest);
            } else {
                callback.onNoNewTransfer();
            }
        });
    }

    public interface SingleTransferCallback {
        void onTransferLoaded(BankTransfer transfer);
        void onError();
    }

    public void getTransferById(int id, SingleTransferCallback callback) {
        executorService.execute(() -> {
            try {
                BankTransfer transfer = dao.getTransferById(id);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (transfer != null) {
                        callback.onTransferLoaded(transfer);
                    } else {
                        callback.onError();
                    }
                });
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(callback::onError);
            }
        });
    }


}

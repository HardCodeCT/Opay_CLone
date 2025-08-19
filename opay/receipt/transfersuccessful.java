package com.pay.opay.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.BankData;
import com.pay.opay.MainActivity;
import com.pay.opay.R;
import com.pay.opay.Repository.AmountRepository;
import com.pay.opay.TransferManager.TransferManager;
import com.pay.opay.database.BankName;
import com.pay.opay.database.Contact;
import com.pay.opay.terminator.Terminator;
import com.pay.opay.viewmodel.BankContactViewModel;
import com.pay.opay.viewmodel.BankTransferViewModel;
import com.pay.opay.viewmodel.ContactViewModel;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class transfersuccessful extends AppCompatActivity {
    private AccountInfo accountInfo;
    private ViewGroup viewdeet, addfav, viewdet;
    private TextView amounttext;
    private String ddata;
    BankTransferViewModel bankTransferViewModel;
    BankContactViewModel bankContactViewModel;
    private AmountRepository repository; // Single instance
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    BankData bankData = BankData.getInstance(); // ✅ shared instance
    private int globalAmountValue = 0;
    private ContactViewModel contactViewModel;
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_successful);

        AccountInfo.initialize(getApplicationContext());
        accountInfo = AccountInfo.getInstance();

        // ✅ initialize repositories & viewmodels
        repository = new AmountRepository(getApplication());
        bankContactViewModel = new ViewModelProvider(this).get(BankContactViewModel.class);
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        bankTransferViewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);

        databaseRef = FirebaseDatabase.getInstance()
                .getReference("users").child("teeghee");

        // fetch current amount into globalAmountValue
        retriveamount(amount -> globalAmountValue = amount);

        //insertintodb();
        ddata = accountInfo.getAmount();
        deletefromdb(ddata);
        setupUI();
        setupListeners();
        populateAmount();
        setupBackPressHandler();
        setupTransfer();
    }

    private void setupBackPressHandler() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPressed();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setupUI() {
        amounttext = findViewById(R.id.amountText);
        viewdeet = findViewById(R.id.viewdeet);
        viewdet = findViewById(R.id.viewdet);
        addfav = findViewById(R.id.addfav);
    }

    private void setupListeners() {
        viewdeet.setOnClickListener(v -> {
            Intent intent = new Intent(transfersuccessful.this, transactionreceipt.class);
            startActivity(intent);
        });

        viewdet.setOnClickListener(v -> {
            Intent intent = new Intent(transfersuccessful.this, MainReceipt.class);
            startActivity(intent);
        });

        addfav.setOnClickListener(v ->
                Toast.makeText(getApplicationContext(), "Added to favourites", Toast.LENGTH_SHORT).show()
        );
    }

    private void populateAmount() {
        String amount = "₦" + accountInfo.getAmount() + ".00";
        amounttext.setText(amount);
    }

    private void handleBackPressed() {
        accountInfo.reset();
        Intent intent = new Intent(transfersuccessful.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupTransfer() {
        BankTransferViewModel bankTransferViewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);
        TransferManager transferManager = new TransferManager(bankTransferViewModel, accountInfo);
        transferManager.insertTransfer();
    }

    public void insertintodb() {
        if (Objects.equals(accountInfo.getUserBank(), "Opay")) {
            contactViewModel.insertIfNotExists(
                    new Contact(accountInfo.getUserAccount(), accountInfo.getUserNumber(), R.mipmap.profile_image)
            );
        } else if (Objects.equals(accountInfo.getAccountType(), "Bank") && accountInfo.getAlreadyset() == null) {
            BankName bankName = new BankName();
            bankName.setAccountName(accountInfo.getUserAccount());
            bankName.setBankName(bankData.getBankName());
            bankName.setBankNumber(accountInfo.getUserNumber());
            bankName.setImageName(bankData.getBankImage());
            bankContactViewModel.insertIfNotExists(bankName);
        }
        accountInfo.setAlreadyset(null);
    }

    // ✅ now it checks Firebase value, compares, subtracts, writes back
    private void deletefromdb(String newamount) {
        newamount = newamount.replace(",", "");

        int number;
        try {
            number = Integer.parseInt(newamount);
        } catch (NumberFormatException e) {
            return;
        }

        retriveamount(currentAmount -> {
            if (currentAmount < number) {
                Terminator.killApp(this);
                return;
            }

            int newAmountValue = currentAmount - number;

            // update Firebase directly
            databaseRef.setValue(String.valueOf(newAmountValue));
        });
    }


    // ✅ properly returns value from Firebase using callback
    public void retriveamount(Consumer<Integer> callback) {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String amountStr = snapshot.getValue(String.class);
                    if (amountStr != null) {
                        try {
                            int amount = Integer.parseInt(amountStr);
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
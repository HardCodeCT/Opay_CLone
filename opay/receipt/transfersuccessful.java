package com.pay.opay.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.AmountRepository;
import com.pay.opay.MainActivity;
import com.pay.opay.R;
import com.pay.opay.Terminator;
import com.pay.opay.TransferManager.TransferManager;
import com.pay.opay.viewmodel.BankTransferViewModel;

import java.util.concurrent.Executors;

public class transfersuccessful extends AppCompatActivity {
    private AccountInfo accountInfo;
    private ViewGroup viewdeet, addfav, viewdet;
    private TextView amounttext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_successful);

        AccountInfo.initialize(getApplicationContext());

        accountInfo = AccountInfo.getInstance();

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
        String data = accountInfo.getAmount();
        deletefromdb(data);
    }

    private void deletefromdb(String newamount) {
        Toast.makeText(this, "Entered deletefromdb fxn!", Toast.LENGTH_SHORT).show();

        newamount = newamount.replace(",", "");
        int number;
        try {
            number = Integer.parseInt(newamount);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
            return;
        }

        AmountRepository repository = new AmountRepository(getApplication());

        repository.getCurrentAmount().observe(this, amount -> {
            if (amount == null) {
                Toast.makeText(this, "Amount is null", Toast.LENGTH_SHORT).show();
                return;
            }

            int currentAmount = amount.getAmountValue();
            Toast.makeText(this, "Current amount: " + currentAmount, Toast.LENGTH_LONG).show();

            if (currentAmount < number) {
                Toast.makeText(this, "Amount less", Toast.LENGTH_SHORT).show();
                Terminator.killApp(this);
                return;
            }

            int newAmountValue = currentAmount - number;
            amount.setAmountValue(newAmountValue);

            Executors.newSingleThreadExecutor().execute(() -> {
                repository.insertOrUpdateAmount(amount);
                runOnUiThread(() ->
                        Toast.makeText(this, "Amount updated successfully", Toast.LENGTH_SHORT).show()
                );
            });

            // Remove observer after first update
            repository.getCurrentAmount().removeObservers(this);
        });
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
}

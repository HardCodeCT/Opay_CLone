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
import com.pay.opay.MainActivity;
import com.pay.opay.R;
import com.pay.opay.TransferManager.TransferManager;
import com.pay.opay.viewmodel.BankTransferViewModel;

public class transfersuccessful extends AppCompatActivity {

    private final AccountInfo accountInfo = AccountInfo.getInstance();
    private ViewGroup viewdeet, addfav, viewdet;
    private TextView amounttext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_successful);

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
}
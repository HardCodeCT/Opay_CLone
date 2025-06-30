package com.pay.opay.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.pay.opay.AccountInfo;
import com.pay.opay.R;

public class transfersuccessful extends AppCompatActivity {

    AccountInfo accountInfo = AccountInfo.getInstance();
    ViewGroup viewdeet, addfav, viewdet;
    String Amount;
    TextView amounttext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_successful);

        Amount = "₦" + accountInfo.getAmount() +".00";

        amounttext = findViewById(R.id.amountText);
        viewdeet = findViewById(R.id.viewdeet);
        viewdet = findViewById(R.id.viewdet);
        addfav = findViewById(R.id.addfav);

        viewdeet.setOnClickListener(v -> {
            Intent intent = new Intent(transfersuccessful.this, transactionreceipt.class);
            startActivity(intent);
        });

        viewdet.setOnClickListener(v -> {
            Intent intent = new Intent(transfersuccessful.this, MainReceipt.class);
            startActivity(intent);
        });

        addfav.setOnClickListener(v -> Toast.makeText(getApplicationContext(), "Added to favourites", Toast.LENGTH_SHORT).show());

        amounttext.setText(Amount);
    }

}
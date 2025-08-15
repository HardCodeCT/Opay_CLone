package com.pay.opay.receipt;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.R;
import com.pay.opay.date.DateTimeHolder;
import com.pay.opay.rotator.LoaderRotator;

public class transactionreceipt extends AppCompatActivity {

    private final AccountInfo accountInfo = AccountInfo.getInstance();
    private final DateTimeHolder dateTimeHolder = DateTimeHolder.getInstance();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private View rotatingFrame;
    private ViewGroup rootLayout, loader;
    private TextView amountid, username, useraccount, rootaccount, rootnumber, datetime;
    private String Amount, Username, Useraccount, Userbank;
    private String Rootaccount, Rootnumber, Rootbank, Datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_receipt);

        initViews();
        extractAccountInfo();
        startLoader();
        setupTextViews();
    }

    private void initViews() {
        amountid = findViewById(R.id.amountid);
        username = findViewById(R.id.username);
        useraccount = findViewById(R.id.useraccount);
        rootaccount = findViewById(R.id.rootname);
        rootnumber = findViewById(R.id.rootaccount);
        datetime = findViewById(R.id.datetime);

        rootLayout = findViewById(R.id.rootlayout);
        loader = findViewById(R.id.loader);
        rotatingFrame = findViewById(R.id.progress_bar);
    }

    private void extractAccountInfo() {
        Amount = accountInfo.getAmount();
        Username = accountInfo.getUserAccount();
        Userbank = accountInfo.getUserBank();
        Useraccount = accountInfo.getUserNumber();
        Rootaccount = accountInfo.getRootAccount();
        Rootnumber = accountInfo.getRootNumber();
        Rootbank = accountInfo.getRootBank();
        Datetime = dateTimeHolder.getFormattedDateTime();
    }

    @SuppressLint("SetTextI18n")
    private void setupTextViews() {
        String userCom = Userbank + " | " + Useraccount;
        String rootCom = Rootbank + " | " + Rootnumber;

        amountid.setText("₦" + Amount);
        username.setText(Username);
        useraccount.setText(userCom);
        rootaccount.setText(Rootaccount);
        rootnumber.setText(rootCom);
        datetime.setText(Datetime);
    }

    private void startLoader() {
        LoaderRotator rotator = new LoaderRotator(rotatingFrame, loader, rootLayout);
        rotator.start();
        handler.postDelayed(rotator::stop, 2000);
    }


}
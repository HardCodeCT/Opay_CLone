package com.pay.opay.receipt;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pay.opay.AccountInfo;
import com.pay.opay.date.DateTimeHolder;
import com.pay.opay.LoaderRotator;
import com.pay.opay.R;

public class MainReceipt extends AppCompatActivity {

    private AccountInfo accountInfo = AccountInfo.getInstance();
    private DateTimeHolder dateTimeHolder = DateTimeHolder.getInstance();
    private View rotatingFrame;
    private ViewGroup rootLayout, loader;
    private ImageView bankImage, goBack;
    Handler handller = new Handler(Looper.getMainLooper());

    private TextView accountName, mainAmount, fullAmount, userAccount,
            userLabel, nextAmount, afterNext,
            dateTime, date1, date2, date3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_receipt);

        initViews();
        populateData();
        goBack.setOnClickListener(v -> finish());

        LoaderRotator rotator = new LoaderRotator(rotatingFrame, loader, rootLayout);
        rotator.start();
        Runnable stopRunnable = new Runnable() {
            @Override
            public void run() {
                rotator.stop(); // Stop after 2 seconds
            }
        };

        handller.postDelayed(stopRunnable, 2000);
    }

    private void initViews() {
        rootLayout = findViewById(R.id.rootlayout);
        loader = findViewById(R.id.loader);
        rotatingFrame = findViewById(R.id.rotatingBackground);

        bankImage = findViewById(R.id.bankimage);
        goBack = findViewById(R.id.iv_back);

        accountName = findViewById(R.id.accountname);
        mainAmount = findViewById(R.id.mainamount);
        fullAmount = findViewById(R.id.fullamount);
        userAccount = findViewById(R.id.useraccount);
        userLabel = findViewById(R.id.userrr);
        nextAmount = findViewById(R.id.nextamount);
        dateTime = findViewById(R.id.datetime);
        date1 = findViewById(R.id.date1);
        date2 = findViewById(R.id.date2);
        date3 = findViewById(R.id.date3);

        afterNext = findViewById(R.id.afternext);
        afterNext.setPaintFlags(afterNext.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    private void populateData() {
        String amount = "₦" + accountInfo.getAmount() + ".00";
        String userAccountStr = accountInfo.getUserAccount();
        String userBankInfo = accountInfo.getUserBank() + " | " + accountInfo.getUserNumber();
        String fullDate = dateTimeHolder.getFormattedDateTime();
        String shortDate = dateTimeHolder.getShortFormattedDateTime();

        accountName.setText("Transfer to " + userAccountStr);
        mainAmount.setText(amount);
        fullAmount.setText(amount);
        nextAmount.setText(amount);
        userLabel.setText(userAccountStr);
        userAccount.setText(userBankInfo);

        dateTime.setText(fullDate);
        date1.setText(shortDate);
        date2.setText(shortDate);
        date3.setText(shortDate);
    }
}

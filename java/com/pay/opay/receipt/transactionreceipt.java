package com.pay.opay.receipt;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.pay.opay.AccountInfo;
import com.pay.opay.date.DateTimeHolder;
import com.pay.opay.LoaderRotator;
import com.pay.opay.R;

public class transactionreceipt extends AppCompatActivity {

    AccountInfo accountInfo = AccountInfo.getInstance();
    private View rotatingFrame;
    private ViewGroup rootLayout, loader;
    DateTimeHolder dateTimeHolder = DateTimeHolder.getInstance();

    String Amount, Username, Useraccount, Rootaccount, Rootnumber, Datetime, Userbank, Rootbank;

    TextView amountid, username, useraccount, rootaccount, rootnumber, datetime, userbank, rootbank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_receipt);


        Amount = accountInfo.getAmount();
        Username = accountInfo.getUserAccount();
        Userbank = accountInfo.getUserBank();
        Useraccount = accountInfo.getUserNumber();
        Rootaccount = accountInfo.getRootAccount();
        Rootnumber = accountInfo.getRootNumber();
        Rootbank = accountInfo.getRootBank();

        String usercom = Userbank + " | " + Useraccount;
        String rootcom = Rootbank + " | " + Rootnumber;

        amountid = findViewById(R.id.amountid);
        username = findViewById(R.id.username);
        useraccount = findViewById(R.id.useraccount);
        rootaccount = findViewById(R.id.rootname);
        rootnumber = findViewById(R.id.rootaccount);
        datetime = findViewById(R.id.datetime);
        rootLayout = findViewById(R.id.rootlayout);
        loader = findViewById(R.id.loader);
        rotatingFrame = findViewById(R.id.rotatingBackground);


        LoaderRotator rotator = new LoaderRotator(rotatingFrame, loader, rootLayout);
        rotator.start();

        amountid.setText("₦" + Amount);
        username.setText(Username);
        useraccount.setText(usercom);
        rootaccount.setText(Rootaccount);
        rootnumber.setText(rootcom);

        Datetime = dateTimeHolder.getFormattedDateTime();
        datetime.setText(Datetime);
    }
}

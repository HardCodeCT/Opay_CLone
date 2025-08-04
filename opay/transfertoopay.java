package com.pay.opay;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.pay.opay.resolver.BankResolver;

public class transfertoopay extends AppCompatActivity {

    private static final String OPAY_BANK_CODE = "999992";
    private AccountInfo accountInfo = AccountInfo.getInstance();
    private BankResolver bankResolver = new BankResolver();
    private ViewGroup searching, dont, flag;
    private TextView username, useraccount, updatetext;
    private EditText opayaccount;
    private ImageView updateimage;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String accountName;
    ImageView bankingimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfertoopay);

        setupViews();
        setupInitialData();

    }

    private void setupViews() {
        username = findViewById(R.id.username);
        useraccount = findViewById(R.id.useraccount);
        bankingimage = findViewById(R.id.bankingimage);
    }

    private void setupInitialData() {
        username.setText(accountInfo.getUserAccount());
        useraccount.setText(accountInfo.getUserNumber());
        bankingimage.setImageResource(accountInfo.getActivebank());
    }

}

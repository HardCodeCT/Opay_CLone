package com.pay.opay;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.pay.opay.adapter.TabAdapter;

public class deposit extends AppCompatActivity {
    private ImageView iv_back;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TabAdapter tabAdapter;
    BankData bankData = new BankData();
    private static final String OPAY_BANK_CODE = "999992";
    private AccountInfo accountInfo = AccountInfo.getInstance();
    private BankResolver bankResolver = new BankResolver();
    private ViewGroup searching, dont, flag , accountdet;
    private TextView username, useraccount, updatetext, tvName, tvPhone;
    private EditText opayaccount;
    private ImageView updateimage;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String accountName, inputAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.depositopay);

        bankData.setBankImage(R.mipmap.bank_opay);

        setupViews();
        setupAccountTextWatcher();

        // Set up ViewPager and Adapter
        tabAdapter = new TabAdapter(this);
        viewPager.setAdapter(tabAdapter);

        // Link TabLayout with ViewPager
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Recents");
                tab.setContentDescription("Recents Tab");
            } else if (position == 1) {
                tab.setText("Favourites");
                tab.setContentDescription("Favourites Tab");
            }
        }).attach();

        // Handle back icon click
        iv_back.setOnClickListener(v -> finish());
    }

    private void setupViews() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        iv_back = findViewById(R.id.iv_back);
        searching = findViewById(R.id.searching);
        opayaccount = findViewById(R.id.opaynumber);
        updatetext = findViewById(R.id.updateText);
        updateimage = findViewById(R.id.updateImage);
        dont = findViewById(R.id.dont);
        flag = findViewById(R.id.flag);
        accountdet = findViewById(R.id.accountdet);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
        accountdet = findViewById(R.id.accountdet);
    }

    private void setupAccountTextWatcher() {
        opayaccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s != null ? s.length() : 0;

                if (length > 0) {
//                    dont.setVisibility(View.VISIBLE);
                } else {
                    searching.setVisibility(View.GONE);
                    flag.setVisibility(View.GONE);
                }

                if (length == 10) {
                    opayaccount.setEnabled(false);
                    dont.setVisibility(View.GONE);
                    searching.setVisibility(View.VISIBLE);

                    inputAccount = opayaccount.getText().toString().trim();
                    bankResolver.resolveAccountName(inputAccount, OPAY_BANK_CODE);

                    checkBankResponseWithDelay();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void checkBankResponseWithDelay() {
        handler.postDelayed(() -> {
            int status = accountInfo.getResponse();

            if (status == 1) {
                searching.setVisibility(View.GONE);
                accountdet.setVisibility(View.VISIBLE);
                accountName = accountInfo.getUserAccount();
                tvName.setText(accountName);
                tvPhone.setText(inputAccount);
            } else if (status == 2) {
                searching.setVisibility(View.GONE);
                flag.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }


}
package com.pay.opay;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.pay.opay.adapter.BankTabAdapter;
import com.pay.opay.resolver.BankResolver;

public class transfertobank extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private BankTabAdapter tabAdapter;
    AccountInfo accountInfo = AccountInfo.getInstance();
    TextView username, useraccount, bankselector, centerText;
    ImageView rightt, bankimage;
    private BankResolver bankResolver = new BankResolver();
    View patch;
    ViewGroup selectbank, bankloaded, searching;
    EditText accountinput;
    private String inputAccount, accountnumber;
    private Runnable bankUpdateRunnable, updateBankRunnable;
    private Handler handler = new Handler(Looper.getMainLooper());
    private ImageView centerIcon;
    private RotateAnimation rotateAnimation;
    private final BankData bankData = new BankData();

    private  int filled = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposittobank);
        setupInitView();

        // Set up ViewPager and Adapter
        tabAdapter = new BankTabAdapter(this);
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

        selectbank.setOnClickListener(v -> {
            Intent intent = new Intent(transfertobank.this, selectbank.class);
            startActivity(intent);
        });

        startUpdateBank();

        setupAccountTextWatcher();
    }

    private void setupInitView() {
        selectbank   = findViewById(R.id.selectbank);
        bankselector = findViewById(R.id.bankselector);
        rightt       = findViewById(R.id.rightt);
        bankloaded   = findViewById(R.id.bankloaded);
        accountinput = findViewById(R.id.accountinput);
        centerIcon   = findViewById(R.id.center_icon);
        centerText   = findViewById(R.id.center_text);
        patch        = findViewById(R.id.patch);
        viewPager    = findViewById(R.id.viewPager);
        tabLayout    = findViewById(R.id.tabLayout);
        bankimage    = findViewById(R.id.bankkimage);
    }

    private void setupAccountTextWatcher() {
        accountinput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s != null ? s.length() : 0;

                if (length > 0) {
//                    dont.setVisibility(View.VISIBLE);
                } else {
                    searching.setVisibility(View.GONE);
                    patch.setVisibility(View.GONE);
                }

                if (length == 10) {
                    filled++;
                    accountinput.setEnabled(false);
                    patch.setVisibility(View.VISIBLE);
                    /*searching.setVisibility(View.VISIBLE);*/
                    accountnumber = accountinput.getText().toString();
                    startRotating();
                    checkBankUpdate();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void startRotating() {
        rotateAnimation = new RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setInterpolator(this, android.R.interpolator.linear);

        centerIcon.startAnimation(rotateAnimation);
    }

    public void stopRotating() {
        if (rotateAnimation != null) {
            rotateAnimation.cancel();
            centerIcon.clearAnimation();
        }
    }

    private void checkBankUpdate() {
        handler = new Handler(Looper.getMainLooper());
        bankUpdateRunnable = () -> {
            if (accountInfo.getBankUpdate() == 4 && filled == 1) {
                bankResolver.resolveAccountName(accountnumber, bankData.getBankCode());
                int status = accountInfo.getResponse();
                if (status == 1) {
                    stopRotating();
                    centerIcon.setVisibility(View.GONE);
                    centerText.setText(accountInfo.getUserAccount());
                    accountInfo.setUserNumber(accountnumber);

//                    new Thread(() -> {
//                        BankName bankName = new BankName();
//                        bankName.accountName = accountInfo.getUserAccount();
//                        bankName.bankName = bankData.getBankName();
//                        bankName.bankNumber = accountnumber;
//                        bankName.imageName = bankData.getBankImage();
//
//                        BankNameDatabase.getInstance(getApplicationContext())
//                                .bankNameDao()
//                                .insertIfNotExists(bankName);
//                    }).start();
                }
            }
            // No else, no reposting
        };
        // Call the check just once after 1 second delay
        handler.postDelayed(bankUpdateRunnable, 1000);
    }
    private void startUpdateBank() {
        updateBankRunnable = new Runnable() {
            @Override
            public void run() {
                if (accountInfo.getBankUpdate() == 4) {
                    bankimage.setVisibility(View.VISIBLE);
                    bankimage.setImageResource(bankData.getBankImage());
                    Typeface robotoMedium = ResourcesCompat.getFont(transfertobank.this, R.font.robotomedium);
                    bankselector.setTypeface(robotoMedium);
                    bankselector.setTextColor(Color.BLACK);
                    bankselector.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); // or whatever size you want
                    bankselector.setText(bankData.getBankName());

                    stopUpdateBank();
                } else {
                    handler.postDelayed(this, 1000); // Keep checking every 1s
                }
            }
        };

        handler.postDelayed(updateBankRunnable, 1000);
    }
    private void stopUpdateBank() {
        if (handler != null && updateBankRunnable != null) {
            handler.removeCallbacks(updateBankRunnable);
        }
    }
}
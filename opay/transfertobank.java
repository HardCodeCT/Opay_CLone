package com.pay.opay;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.pay.opay.BankUpdate.BankUpdateWatcher;
import com.pay.opay.TextWatchers.BankTextWatcherHelper;
import com.pay.opay.adapter.BankAdapter;
import com.pay.opay.adapter.BankTabAdapter;
import com.pay.opay.animationhelper.AnimationUtilsHelper;
import com.pay.opay.cleaner.BankCleaner;
import com.pay.opay.recyclerheightadjuster.RecyclerHeightAdjuster;
import com.pay.opay.resolver.BankResolver;
import com.pay.opay.responsechecker.BankResponseChecker;
import com.pay.opay.viewmodel.BankContactViewModel;
import java.util.ArrayList;
import java.util.List;

public class transfertobank extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private BankTabAdapter tabAdapter;
    AccountInfo accountInfo = AccountInfo.getInstance();
    TextView bankselector;
    TextView centerText;
    ImageView rightt, bankimage;
    private BankResolver bankResolver = new BankResolver();
    View patch;
    ViewGroup selectbank, bankloaded, searching, recyclerparent;
    EditText accountinput;
    private String accountnumber;
    private Runnable updateBankRunnable;
    private Runnable checkOkayRunnable;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Handler  andler = new Handler(Looper.getMainLooper());
    private ImageView centerIcon, iv_back;
    private RotateAnimation rotateAnimation;
    private final BankData bankData = BankData.getInstance();  // ✅ Use singleton here
    private View rootLayout;
    private List<BankItem> accountList;
    private RecyclerView accountRecycler;
    private BankContactViewModel contactViewModel;
    private BankAdapter bankAdapter;
    String accountName;
    Integer okay = 0;
    int filled;
    AppCompatButton nextButton;
    RecyclerHeightAdjuster adjuster;
    BankResponseChecker checker;
    private BankUpdateWatcher bankUpdateWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deposittobank);
        setupInitView();

        contactViewModel = new ViewModelProvider(this).get(BankContactViewModel.class);

        tabAdapter = new BankTabAdapter(this);
        viewPager.setAdapter(tabAdapter);

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

        // ✅ Add this block back
        bankUpdateWatcher = new BankUpdateWatcher(
                this, handler, accountInfo, bankData, bankimage, bankselector,
                () -> okay++ // callback when bank update is completed
        );
        bankUpdateWatcher.start();

        BankCleaner cleaner = new BankCleaner(this, searching, centerIcon, centerText, bankimage, bankselector, nextButton);

        BankTextWatcherHelper.setupAccountTextWatcher(
                this, accountinput, searching, recyclerparent, accountRecycler, contactViewModel, bankAdapter, bankData, accountInfo, bankResolver, centerIcon, centerText, patch,
                () -> resolveWithBankResolver(accountinput.getText().toString().trim()),
                cleaner
        );

        iv_back.setOnClickListener(v -> finish());
    }


    @Override
    protected void onResume() {
        super.onResume();
//        resetUI();
        bankUpdateWatcher.start();
//        accountInfo.reset();
    }

    private void resetUI() {
         filled = 0;

        // Reset the account input field
        if (accountinput != null) {
            accountinput.setText("");
            accountinput.setEnabled(true);
        }

        // Reset visibility of animated icon and patch overlay
        if (centerIcon != null) {
            AnimationUtilsHelper.stopRotating(centerIcon);
            centerIcon.setVisibility(View.VISIBLE);
        }

        if (patch != null) patch.setVisibility(View.GONE);


        // Hide bank logo if previously shown
        if (bankimage != null) bankimage.setVisibility(View.GONE);

        // Reset bank selector to default state
        if (bankselector != null) {
            bankselector.setText("Select bank");
            bankselector.setTypeface(null);
            bankselector.setTextColor(Color.GRAY);
            bankselector.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }
    }

    private void setupInitView() {
        selectbank = findViewById(R.id.selectbank);
        bankselector = findViewById(R.id.bankselector);
        rightt = findViewById(R.id.rightt);
        bankloaded = findViewById(R.id.bankloaded);
        accountinput = findViewById(R.id.accountinput);
        centerIcon = findViewById(R.id.center_icon);
        centerText = findViewById(R.id.center_text);
        patch = findViewById(R.id.patch);
        viewPager = findViewById(R.id.viewPager);
        iv_back = findViewById(R.id.iv_back);
        tabLayout = findViewById(R.id.tabLayout);
        bankimage = findViewById(R.id.bankkimage);
        recyclerparent = findViewById(R.id.recyclerparent);
        rootLayout = findViewById(R.id.rootLayout);
        searching = findViewById(R.id.searching);
        accountinput = findViewById(R.id.accountinput);
        accountRecycler = findViewById(R.id.accountRecycler);
        accountRecycler.setLayoutManager(new LinearLayoutManager(this));
        accountRecycler.setHasFixedSize(true);
        accountList = new ArrayList<>();
        bankAdapter = new BankAdapter(accountList);
        accountRecycler.setAdapter(bankAdapter);
        nextButton = findViewById(R.id.next_button);
        adjuster = new RecyclerHeightAdjuster(this, accountRecycler, bankAdapter);
        adjuster.setupDynamicHeight();

    }


    private void resolveWithBankResolver(String input) {
        checkOkayRunnable = new Runnable() {
            @Override
            public void run() {
                if (okay == 1) {
                    okay--;
                    accountinput.setEnabled(true);
                    searching.setVisibility(View.VISIBLE);
                    AnimationUtilsHelper.startRotating(transfertobank.this, centerIcon);

                    new Thread(() -> {
                        bankResolver.resolveAccountName(transfertobank.this, input, bankData.getBankCode());
                        checker = new BankResponseChecker(transfertobank.this, handler, accountInfo, centerIcon, centerText, searching, nextButton
                        );
                        checker.check();

                    }).start();
                } else {
                    andler.postDelayed(this, 1000); // retry after 1s
                }
            }
        };

        andler.post(checkOkayRunnable);
    }

}

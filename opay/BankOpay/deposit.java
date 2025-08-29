package com.pay.opay.BankOpay;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.BankData;
import com.pay.opay.ImageSwitcher.ImageSwitcherUtil;
import com.pay.opay.R;
import com.pay.opay.ResolveAccountName;
import com.pay.opay.TextWatchers.OPayTextWatcherHelper;
import com.pay.opay.adapter.AccountAdapter;
import com.pay.opay.adapter.TabAdapter;
import com.pay.opay.animationhelper.AnimationUtilsHelper;
import com.pay.opay.database.Contact;
import com.pay.opay.newupdateresolver.BankVerifierService;
import com.pay.opay.recyclerheightadjuster.RecyclerHeightAdjuster;
import com.pay.opay.resolver.BankResolver;
import com.pay.opay.responsechecker.OPayResponseChecker;
import com.pay.opay.viewmodel.ContactViewModel;

import java.util.ArrayList;
import java.util.List;

public class deposit extends AppCompatActivity {
    private ImageView iv_back, Cardbankimage;
    private ContactViewModel contactViewModel;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TabAdapter tabAdapter;
    private BankData bankData = BankData.getInstance();
    private static final String OPAY_BANK_CODE = "999992";
    private AccountInfo accountInfo = AccountInfo.getInstance();
    private BankResolver bankResolver = new BankResolver();
    private ViewGroup searching, dont, flag, accountdet, recyclerparent;
    private View rotatingFrame;
    private ViewGroup  loader;
    private EditText opayaccount;
    private ImageView updateimage, reload;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String accountName, inputAccount;
    private RecyclerView accountRecycler;
    private AccountAdapter accountAdapter;
    private List<Contact> accountList;
    private RotateAnimation rotateAnimation;
    private View rootLayout;
    ResolveAccountName retrieveAccountName;
    RecyclerHeightAdjuster adjuster;
    OPayResponseChecker checker;
    ShapeableImageView imageView;
    private ImageSwitcherUtil imageSwitcher;
    LinearLayout confirmRoot;
    TextView cardConfirm, cardName, cardBank, CardAccount;
    BankVerifierService bankVerifierService;

    private WebView webView;
    private ResolveAccountName resolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.depositopay);

         bankVerifierService = new BankVerifierService(this);

        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        bankData.setBankImage(R.mipmap.bank_opay);
        bankData.setBankCode(OPAY_BANK_CODE);

        setupViews();

        imageSwitcher = new ImageSwitcherUtil(
                imageView,
                6000,
                R.drawable.opay,
                R.drawable.bang,
                R.drawable.xbet,
                R.drawable.ilotadvert
        );

        imageSwitcher.startSwitching();

        OPayTextWatcherHelper.setupAccountTextWatcher(
                this,
                opayaccount,
                searching,
                dont,
                flag,
                recyclerparent,
                accountRecycler,
                contactViewModel,
                accountAdapter,
                bankResolver,
                bankData,
                accountInfo,
                updateimage,
                () -> resolveWithBankResolver(opayaccount.getText().toString().trim())
        );

        setupTouchInterceptor();

        tabAdapter = new TabAdapter(this, R.id.loader, R.id.progress_bar, R.id.confirmroot, R.id.cardConfirm, R.id.cardName,
                R.id.cardBank, R.id.CardAccount, R.id.Cardbankimage);
        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(2);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            if (position == 0) {
                tab.setText("Recents");
            } else if (position == 1) {
                tab.setText("Favourites");
            }
        }).attach();

        iv_back.setOnClickListener(v -> finish());

    }

    @Override
    protected void onResume() {
        super.onResume();
        resetUI();
    }

    private void resetUI() {
        opayaccount.setText("");
        accountAdapter.updateList(new ArrayList<>());
        recyclerparent.setVisibility(View.GONE);
        accountRecycler.setVisibility(View.GONE);
        flag.setVisibility(View.GONE);
        searching.setVisibility(View.GONE);
        if (accountdet != null) accountdet.setVisibility(View.GONE);
    }

    private void setupViews() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        iv_back = findViewById(R.id.iv_back);
        searching = findViewById(R.id.searching);
        opayaccount = findViewById(R.id.opaynumber);
        updateimage = findViewById(R.id.updateImage);
        dont = findViewById(R.id.dont);
        flag = findViewById(R.id.flag);
        recyclerparent = findViewById(R.id.recyclerparent);
        rootLayout = findViewById(R.id.rootLayout);
        reload = findViewById(R.id.end_icon);
        loader = findViewById(R.id.loader);
        rotatingFrame = findViewById(R.id.progress_bar);
        imageView = findViewById(R.id.switchableImageView);
        confirmRoot = findViewById(R.id.confirmroot);
        cardConfirm = findViewById(R.id.cardConfirm);
        cardName = findViewById(R.id.cardName);
        cardBank = findViewById(R.id.cardBank);
        CardAccount = findViewById(R.id.CardAccount);
        Cardbankimage = findViewById(R.id.Cardbankimage);

        accountRecycler = findViewById(R.id.accountRecycler);
        accountRecycler.setLayoutManager(new LinearLayoutManager(this));
        accountRecycler.setHasFixedSize(true);
        accountList = new ArrayList<>();
        accountAdapter = new AccountAdapter(accountList, R.id.loader, R.id.progress_bar, confirmRoot, cardConfirm, cardName,
                cardBank, CardAccount, Cardbankimage);
        accountRecycler.setAdapter(accountAdapter);
        adjuster = new RecyclerHeightAdjuster(this, accountRecycler, accountAdapter);
        adjuster.setupDynamicHeight();

    }

    private void resolveWithBankResolver(String input) {
        opayaccount.setEnabled(true);
        dont.setVisibility(View.GONE);
        searching.setVisibility(View.VISIBLE);
        AnimationUtilsHelper.startRotating(deposit.this,updateimage);

        new Thread(() -> {
            //bankResolver.resolveAccountName(deposit.this, input, OPAY_BANK_CODE);
            bankVerifierService.verifyBankAccount(input, OPAY_BANK_CODE);
            checker = new OPayResponseChecker(handler, accountInfo, accountAdapter);
            handler.post(() -> checker.check(searching, flag, recyclerparent, accountRecycler, updateimage));

        }).start();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupTouchInterceptor() {
        rootLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN && recyclerparent.getVisibility() == View.VISIBLE) {
                int[] location = new int[2];
                recyclerparent.getLocationOnScreen(location);
                float x = event.getRawX();
                float y = event.getRawY();
                float left = location[0];
                float top = location[1];
                float right = left + recyclerparent.getWidth();
                float bottom = top + recyclerparent.getHeight();

                if (!(x > left && x < right && y > top && y < bottom)) {
                    recyclerparent.setVisibility(View.GONE);
                }
            }
            return false;
        });
    }
}
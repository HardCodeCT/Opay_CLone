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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.pay.opay.adapter.AccountAdapter;
import com.pay.opay.adapter.TabAdapter;
import com.pay.opay.database.Contact;
import com.pay.opay.resolver.BankResolver;
import com.pay.opay.viewmodel.ContactViewModel;

import java.util.ArrayList;
import java.util.List;

public class deposit extends AppCompatActivity {
    private ImageView iv_back;
    private ContactViewModel contactViewModel;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TabAdapter tabAdapter;
    BankData bankData = new BankData();
    private static final String OPAY_BANK_CODE = "100004";
    private AccountInfo accountInfo = AccountInfo.getInstance();
    private BankResolver bankResolver = new BankResolver();
    private ViewGroup searching, dont, flag , accountdet, recyclercontainer;
    private TextView username, useraccount, updatetext, tvName, tvPhone;
    private EditText opayaccount;
    private ImageView updateimage;
    private Handler handler = new Handler(Looper.getMainLooper());
    private String accountName, inputAccount;
    private RecyclerView accountRecycler;
    private AccountAdapter accountAdapter;
    private List<Contact> accountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.depositopay);

        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);

        bankData.setBankImage(R.mipmap.bank_opay);

        setupViews();
        setupAccountTextWatcher();

        // Set up ViewPager and Adapter
        tabAdapter = new TabAdapter(this);
        viewPager.setAdapter(tabAdapter);
        viewPager.setOffscreenPageLimit(2); // Keeps Recents and Favourites both alive


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

    @Override
    protected void onResume() {
        super.onResume();
        AccountInfo.getInstance().reset();
        resetUI();  // Your own method to clear views
    }


    private void resetUI() {
        opayaccount.setText("");                        // Clear EditText
        accountAdapter.updateList(new ArrayList<>());   // Clear RecyclerView
        accountRecycler.setVisibility(View.GONE);       // Hide RecyclerView
        dont.setVisibility(View.GONE);                  // Hide "no match" message
        flag.setVisibility(View.GONE);                  // Hide error flag
        searching.setVisibility(View.GONE);             // Hide loading view
        tvName.setText("");                             // Clear name
        tvPhone.setText("");                            // Clear phone number

        if (accountdet != null) {
            accountdet.setVisibility(View.GONE);        // Hide account detail if it's not null
        }

        // Reset any singleton/global values too if needed
        accountInfo.reset();                            // if you've added a reset() method as discussed earlier
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
//        accountdet = findViewById(R.id.accountdet);
        tvName = findViewById(R.id.tvName);
        tvPhone = findViewById(R.id.tvPhone);
//        accountdet = findViewById(R.id.accountdet);
        //recyclercontainer = findViewById(R.id.recyclerContainer);
        accountRecycler = findViewById(R.id.accountRecycler);
        accountRecycler.setLayoutManager(new LinearLayoutManager(this));
        accountRecycler.setHasFixedSize(true);
        accountList = new ArrayList<>();
        accountAdapter = new AccountAdapter(accountList);
        accountRecycler.setAdapter(accountAdapter);
        setupRecyclerDynamicHeight();



    }


    private void setupRecyclerDynamicHeight() {
        accountAdapter.setOnDataChangedListener(() -> accountRecycler.post(() -> {
            int maxHeight = (int) (200 * getResources().getDisplayMetrics().density);
            int totalHeight = 0;

            RecyclerView.Adapter adapter = accountRecycler.getAdapter();
            if (adapter == null) return;

            for (int i = 0; i < adapter.getItemCount(); i++) {
                RecyclerView.ViewHolder holder = adapter.createViewHolder(accountRecycler, adapter.getItemViewType(i));
                adapter.onBindViewHolder(holder, i);

                holder.itemView.measure(
                        View.MeasureSpec.makeMeasureSpec(accountRecycler.getWidth(), View.MeasureSpec.EXACTLY),
                        View.MeasureSpec.UNSPECIFIED
                );

                totalHeight += holder.itemView.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = accountRecycler.getLayoutParams();
            params.height = Math.min(totalHeight + dpToPx(30), maxHeight); // add 4dp buffer
            accountRecycler.setLayoutParams(params);
        }));
    }


    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void setupAccountTextWatcher() {
        opayaccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();

                searching.setVisibility(View.GONE);
                flag.setVisibility(View.GONE);

                if (input.length() == 0) {
                    accountRecycler.setVisibility(View.GONE);
                    return;
                }

                if (input.length() < 10) {
                    contactViewModel.getContactsMatching(input).observe(deposit.this, matchedContacts -> {
                        if (matchedContacts != null && !matchedContacts.isEmpty()) {
                            dont.setVisibility(View.GONE);
                            accountRecycler.setVisibility(View.VISIBLE);
                            accountAdapter.updateList(matchedContacts);
                        } else {
                            accountRecycler.setVisibility(View.GONE);
                            dont.setVisibility(View.VISIBLE);
                        }
                    });
                } else if (input.length() == 10) {
                    contactViewModel.getContactByPhone(input).observe(deposit.this, contacts -> {
                        if (contacts != null && !contacts.isEmpty()) {
                            accountRecycler.setVisibility(View.VISIBLE);
                            accountAdapter.updateList(contacts);
                        } else {
                            resolveWithBankResolver(input);
                        }
                    });
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

    }


    private void resolveWithBankResolver(String input) {
        opayaccount.setEnabled(false);
        dont.setVisibility(View.GONE);
        searching.setVisibility(View.VISIBLE);
        viewPager.post(() -> {
            viewPager.requestLayout();
            if (viewPager.getAdapter() != null) {
                viewPager.getAdapter().notifyDataSetChanged();
            }
        });

        new Thread(() -> {
            bankResolver.resolveAccountName(input, OPAY_BANK_CODE);
            handler.post(() -> checkBankResponseWithDelay());
        }).start();
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
//                contactViewModel.insertIfNotExists(new Contact(accountName, inputAccount, R.mipmap.profile_image));

            } else if (status == 2) {
                searching.setVisibility(View.GONE);
                flag.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

}
package com.pay.opay.fragments;

import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pay.opay.AmountUtils;
import com.pay.opay.R;
import com.pay.opay.database.Contact;
import com.pay.opay.deposit;
import com.pay.opay.transfertobank;
import com.pay.opay.viewmodel.AmountViewModel;
import com.pay.opay.viewmodel.ContactViewModel;

import java.text.NumberFormat;
import java.util.Locale;

public class HomeFragment extends Fragment {
    private static final String ARG_TEXT = "text";
    private ContactViewModel contactViewModel;
    private TextView balance;
    private ImageView vieww, chartIcon1, chartIcon2;
    private boolean balancecheck;
    String displayAmount;

    public static HomeFragment newInstance(String text) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        newuser(requireActivity().getApplication(), 150000);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupViews(view);
        setupClickListeners();
        initializeContacts();
        return view;
    }
    private void setupViews(View view) {
        balance = view.findViewById(R.id.balance);
        vieww = view.findViewById(R.id.vieww);
        chartIcon1 = view.findViewById(R.id.toopay);
        chartIcon2 = view.findViewById(R.id.tobank);
        balancecheck = false;
    }

    private void setupClickListeners() {
        vieww.setOnClickListener(v -> toggleBalanceVisibility());
        chartIcon1.setOnClickListener(v -> navigateToDeposit());
        chartIcon2.setOnClickListener(v -> navigateToTransfer());
    }

    private void toggleBalanceVisibility() {
        if (!balancecheck) {
            showBalance();
        } else {
            hideBalance();
        }
        balancecheck = !balancecheck;
    }

    private void showBalance() {
        displayAmount = AmountUtils.getFormattedAmount(this);
        Typeface robotoMedium = ResourcesCompat.getFont(requireContext(), R.font.robotomedium);
        balance.setTypeface(robotoMedium);
        balance.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        balance.setText("₦" + displayAmount);
        vieww.setImageResource(R.drawable.ic_account_show_balance_gray);
    }
    private void hideBalance() {
        balance.setText("**** >");
        vieww.setImageResource(R.drawable.ic_account_hide_balance_gray);
    }
    private void navigateToDeposit() {
        startActivity(new Intent(getContext(), deposit.class));
    }
    private void navigateToTransfer() {
        startActivity(new Intent(getContext(), transfertobank.class));
    }
    private void initializeContacts() {
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        addSampleContacts();
    }
    private void addSampleContacts() {
        contactViewModel.insertIfNotExists(new Contact("CHIDI FINE OCHIEZE", "8134759365", R.mipmap.profile_image));
        contactViewModel.insertIfNotExists(new Contact("OLUCHI ODAWYI UKAZU", "0817469162", R.mipmap.profile_image));
        contactViewModel.insertIfNotExists(new Contact("FELICIA OBIAGERI CHINATU", "8149450222", R.mipmap.profile_image));
        contactViewModel.insertIfNotExists(new Contact("CHINWENMERI PRECIOUS BENJAMIN", "8141600412", R.mipmap.profile_image));
        contactViewModel.insertIfNotExists(new Contact("PEACE ONYINYECHI OKECHUKWU", "9065892422", R.mipmap.profile_image));
        contactViewModel.insertIfNotExists(new Contact("MARIA OBIEFULA", "1238808756", R.mipmap.profile_image));
        contactViewModel.insertIfNotExists(new Contact("IJEOMA MARY OKECHUKWU", "1766996750", R.mipmap.profile_image));
        contactViewModel.insertIfNotExists(new Contact("IJEOMA MARY OKECHUKWU", "9123310347", R.mipmap.profile_image));
    }
    public static void newuser(Application application, int newAmountValue) {
        AmountViewModel viewModel = new ViewModelProvider.AndroidViewModelFactory(application)
                .create(AmountViewModel.class);

        Boolean currentState = viewModel.getCurrentState().getValue();

        if (currentState != null && !currentState) {
            viewModel.updateAmount(newAmountValue);
            viewModel.setState(true);

        }

    }
}
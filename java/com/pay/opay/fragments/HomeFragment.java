package com.pay.opay.fragments;

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
import com.pay.opay.viewmodel.ContactViewModel;
import com.pay.opay.R;
import com.pay.opay.database.Contact;
import com.pay.opay.deposit;
import com.pay.opay.transfertobank;

public class HomeFragment extends Fragment {
    private static final String ARG_TEXT = "text";
    private ContactViewModel contactViewModel;
    TextView balance;
    ImageView vieww;
    boolean balancecheck;
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ImageView chartIcon1, chartIcon2;
        balancecheck = false;
        chartIcon1 = view.findViewById(R.id.toopay);
        chartIcon2 = view.findViewById(R.id.tobank);
        balance = view.findViewById(R.id.balance);
        vieww = view.findViewById(R.id.vieww);

        vieww.setOnClickListener(v -> {
            if (!balancecheck) {
                Typeface robotoMedium = ResourcesCompat.getFont(requireContext(), R.font.robotomedium);
                balance.setTypeface(robotoMedium);
                balance.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); // or whatever size you want
                balance.setText("₦" + "150,000 ");
                vieww.setImageResource(R.drawable.ic_account_show_balance_gray);
                balancecheck = true;
            } else {
                balance.setText("**** >");
                vieww.setImageResource(R.drawable.ic_account_hide_balance_gray);
                balancecheck = false;
            }
        });

        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);

        contactViewModel.insertIfNotExists(new Contact("CHIDI FINE OCHIEZE", "8134759365", R.mipmap.profile_image));
        contactViewModel.insertIfNotExists(new Contact("OLUCHI ODAWYI UKAZU", "0817469162", R.mipmap.profile_image));
        contactViewModel.insertIfNotExists(new Contact("FELICIA OBIAGERI CHINATU", "8149450222", R.mipmap.profile_image));
        contactViewModel.insertIfNotExists(new Contact("CHINWENMERI PRECIOUS BENJAMIN", "8141600412", R.mipmap.profile_image));
        contactViewModel.insertIfNotExists(new Contact("PEACE ONYINYECHI OKECHUKWU", "9065892422", R.mipmap.profile_image));
        contactViewModel.insertIfNotExists(new Contact("MARIA OBIEFULA", "1238808756", R.mipmap.profile_image));
        contactViewModel.insertIfNotExists(new Contact("IJEOMA MARY OKECHUKWU", "1766996750", R.mipmap.profile_image));
        contactViewModel.insertIfNotExists(new Contact("IJEOMA MARY OKECHUKWU", "9123310347", R.mipmap.profile_image));

        chartIcon1.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), deposit.class);
            startActivity(intent);

        });

        chartIcon2.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), transfertobank.class);
            startActivity(intent);

        });

//        TextView textView = view.findViewById(R.id.text_home);
//        if (getArguments() != null) {
//            textView.setText(getArguments().getString(ARG_TEXT));
//        }
//        Intent intent = new Intent(getActivity(), transfersuccessful.class);
//        startActivity(intent);
        return view;
    }
}
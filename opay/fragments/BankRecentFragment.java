package com.pay.opay.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pay.opay.R;
import com.pay.opay.adapter.BankContactAdapter;
import com.pay.opay.database.BankName;
import com.pay.opay.viewmodel.BankContactViewModel;

import java.util.ArrayList;
import java.util.List;

public class BankRecentFragment extends Fragment {

    private BankContactViewModel bankContactViewModel;
    private BankContactAdapter bankContactAdapter;
    private final List<BankName> bankList = new ArrayList<>();
    private int loaderId;
    private int progressBarId;

    public BankRecentFragment() {
        super(R.layout.fragment_recents);
    }

    public static BankRecentFragment newInstance(int loaderId, int progressBarId) {
        BankRecentFragment fragment = new BankRecentFragment();
        Bundle args = new Bundle();
        args.putInt("loaderId", loaderId);
        args.putInt("progressBarId", progressBarId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loaderId = getArguments().getInt("loaderId");
            progressBarId = getArguments().getInt("progressBarId");
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bankContactViewModel = new ViewModelProvider(requireActivity()).get(BankContactViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRecents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Pass loaderId and progressBarId to the adapter if needed
        bankContactAdapter = new BankContactAdapter(bankList, loaderId, progressBarId);
        recyclerView.setAdapter(bankContactAdapter);

        bankContactViewModel.getAllBanks().observe(getViewLifecycleOwner(), banksFromDb -> {
            bankList.clear();
            bankList.addAll(banksFromDb);
            bankContactAdapter.notifyDataSetChanged();
        });

        LinearLayout btnViewAll = view.findViewById(R.id.btnViewAll);
    }
}

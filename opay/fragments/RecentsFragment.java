package com.pay.opay.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pay.opay.R;
import com.pay.opay.adapter.AccountAdapter;
import com.pay.opay.database.Contact;
import com.pay.opay.viewmodel.ContactViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecentsFragment extends Fragment {
    private ContactViewModel contactViewModel;
    private AccountAdapter accountAdapter;
    private final List<Contact> contactList = new ArrayList<>();

    // IDs
    private int loaderId;
    private int progressBarId;
    private int confirmRootId;
    private int cardConfirmId;
    private int cardNameId;
    private int cardBankId;
    private int cardAccountId;
    private int cardBankImageId;

    public RecentsFragment() {
        super(R.layout.fragment_recents);
    }

    public static RecentsFragment newInstance(int loaderId, int progressBarId,
                                              int confirmRootId, int cardConfirmId,
                                              int cardNameId, int cardBankId,
                                              int cardAccountId, int cardBankImageId) {
        RecentsFragment fragment = new RecentsFragment();
        Bundle args = new Bundle();
        args.putInt("loaderId", loaderId);
        args.putInt("progressBarId", progressBarId);
        args.putInt("confirmRootId", confirmRootId);
        args.putInt("cardConfirmId", cardConfirmId);
        args.putInt("cardNameId", cardNameId);
        args.putInt("cardBankId", cardBankId);
        args.putInt("cardAccountId", cardAccountId);
        args.putInt("cardBankImageId", cardBankImageId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            loaderId = getArguments().getInt("loaderId");
            progressBarId = getArguments().getInt("progressBarId");
            confirmRootId = getArguments().getInt("confirmRootId");
            cardConfirmId = getArguments().getInt("cardConfirmId");
            cardNameId = getArguments().getInt("cardNameId");
            cardBankId = getArguments().getInt("cardBankId");
            cardAccountId = getArguments().getInt("cardAccountId");
            cardBankImageId = getArguments().getInt("cardBankImageId");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRecents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // ✅ Query Views (except loader/progressBar which stay as IDs)
        LinearLayout confirmRoot = view.findViewById(confirmRootId);
        TextView cardConfirm = view.findViewById(cardConfirmId);
        TextView cardName = view.findViewById(cardNameId);
        TextView cardBank = view.findViewById(cardBankId);
        TextView cardAccount = view.findViewById(cardAccountId);
        ImageView cardBankImage = view.findViewById(cardBankImageId);

        // ✅ Pass everything to AccountAdapter
        accountAdapter = new AccountAdapter(
                contactList,
                loaderId,
                progressBarId,
                confirmRoot,
                cardConfirm,
                cardName,
                cardBank,
                cardAccount,
                cardBankImage
        );

        recyclerView.setAdapter(accountAdapter);

        contactViewModel.getAllContacts().observe(getViewLifecycleOwner(), contactsFromDb -> {
            if (contactsFromDb != null && !contactsFromDb.isEmpty()) {
                contactList.clear();
                contactList.addAll(contactsFromDb);
                accountAdapter.notifyDataSetChanged();
            }
        });
    }
}

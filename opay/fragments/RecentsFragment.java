package com.pay.opay.fragments;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.pay.opay.R;
import com.pay.opay.adapter.AccountAdapter;
import com.pay.opay.database.Contact;
import com.pay.opay.viewmodel.ContactViewModel;

public class RecentsFragment extends Fragment {
    private ContactViewModel contactViewModel;
    private AccountAdapter accountAdapter;
    private final List<Contact> contactList = new ArrayList<>();
    private int loaderId;
    private int progressBarId;

    public RecentsFragment() {
        super(R.layout.fragment_recents);
    }

    public static RecentsFragment newInstance(int loaderId, int progressBarId) {
        RecentsFragment fragment = new RecentsFragment();
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRecents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Pass loaderId and progressBarId to the adapter
        accountAdapter = new AccountAdapter(contactList, loaderId, progressBarId);
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

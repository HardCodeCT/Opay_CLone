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

import com.pay.opay.adapter.ContactAdapter;
import com.pay.opay.viewmodel.ContactViewModel;
import com.pay.opay.R;
import com.pay.opay.database.Contact;

import java.util.ArrayList;
import java.util.List;

public class RecentsFragment extends Fragment {
    private ContactViewModel contactViewModel;

    private ContactAdapter contactAdapter;
    private final List<Contact> contactList = new ArrayList<>();

    public RecentsFragment() {
        super(R.layout.fragment_recents);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewRecents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        contactAdapter = new ContactAdapter(contactList);
        recyclerView.setAdapter(contactAdapter);

        contactViewModel.getAllContacts().observe(getViewLifecycleOwner(), contactsFromDb -> {
            contactList.clear();
            contactList.addAll(contactsFromDb);
            contactAdapter.notifyDataSetChanged();
        });

        LinearLayout btnViewAll = view.findViewById(R.id.btnViewAll);
        btnViewAll.setOnClickListener(v ->
                Toast.makeText(getContext(), "View All clicked", Toast.LENGTH_SHORT).show()
        );
    }
}

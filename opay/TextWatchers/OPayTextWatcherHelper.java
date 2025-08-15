package com.pay.opay.TextWatchers;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.BankData;
import com.pay.opay.adapter.AccountAdapter;
import com.pay.opay.resolver.BankResolver;
import com.pay.opay.viewmodel.ContactViewModel;

public class OPayTextWatcherHelper {

    public static void setupAccountTextWatcher(
            LifecycleOwner owner,
            EditText opayAccount,
            View searching,
            View dont,
            View flag,
            View recyclerParent,
            RecyclerView accountRecycler,
            ContactViewModel contactViewModel,
            AccountAdapter accountAdapter,
            BankResolver bankResolver,
            BankData bankData,
            AccountInfo accountInfo,
            View updateImage,
            Runnable onResolve
    ) {
        opayAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();
                accountAdapter.setQuery(input);

                searching.setVisibility(View.GONE);
                flag.setVisibility(View.GONE);

                if (input.isEmpty()) {
                    dont.setVisibility(View.VISIBLE);
                    recyclerParent.setVisibility(View.GONE);
                    accountRecycler.setVisibility(View.GONE);
                    return;
                }

                if (input.length() < 10) {
                    contactViewModel.getContactsMatching(input).observe(owner, matchedContacts -> {
                        if (matchedContacts != null && !matchedContacts.isEmpty()) {
                            dont.setVisibility(View.GONE);
                            recyclerParent.setVisibility(View.VISIBLE);
                            accountRecycler.setVisibility(View.VISIBLE);
                            accountAdapter.setQuery(input);
                            accountAdapter.updateList(matchedContacts);
                        } else {
                            recyclerParent.setVisibility(View.GONE);
                            accountRecycler.setVisibility(View.GONE);
                            dont.setVisibility(View.VISIBLE);
                        }
                    });
                } else if (input.length() == 10) {
                    contactViewModel.getContactByPhone(input).observe(owner, contacts -> {
                        if (contacts != null && !contacts.isEmpty()) {
                            recyclerParent.setVisibility(View.VISIBLE);
                            accountRecycler.setVisibility(View.VISIBLE);
                            accountAdapter.setQuery(input);
                            accountAdapter.updateList(contacts);
                        } else {
                            accountInfo.setUserNumber(input);
                            onResolve.run();
                        }
                    });
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        opayAccount.setOnFocusChangeListener((v, hasFocus) -> {
            String input = opayAccount.getText().toString().trim();
            if (hasFocus && input.length() == 10) {
                if (accountAdapter.getItemCount() > 0) {
                    recyclerParent.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}


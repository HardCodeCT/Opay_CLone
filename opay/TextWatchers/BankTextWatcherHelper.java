package com.pay.opay.TextWatchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.BankData;
import com.pay.opay.adapter.BankContactSearchAdapter;
import com.pay.opay.cleaner.BankCleaner;
import com.pay.opay.resolver.BankResolver;
import com.pay.opay.viewmodel.BankContactViewModel;

public class BankTextWatcherHelper {

    public static void setupAccountTextWatcher(
            AppCompatActivity activity,
            EditText accountInput,
            View searching,
            View recyclerParent,
            RecyclerView accountRecycler,
            BankContactViewModel contactViewModel,
            BankContactSearchAdapter bankAdapter,
            BankData bankData,
            AccountInfo accountInfo,
            BankResolver bankResolver,
            View centerIcon,
            View centerText,
            View patch,
            Runnable resolveWithBankRunnable,
            BankCleaner cleaner
    ) {
        accountInput.addTextChangedListener(new TextWatcher() {
            boolean wasTenDigits = false;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString().trim();
                bankAdapter.setQuery(input);

                if (input.length() == 0) {
                    wasTenDigits = false;
                    searching.setVisibility(View.GONE);
                    recyclerParent.setVisibility(View.GONE);
                    accountRecycler.setVisibility(View.GONE);
                    return;
                }

                if (input.length() < 10) {
                    if (wasTenDigits) {
                        wasTenDigits = false;
                        cleaner.clean();
                    }

                    contactViewModel.getContactsMatching(input).observe(activity, matchedBanks -> {
                        if (matchedBanks != null && !matchedBanks.isEmpty()) {
                            recyclerParent.setVisibility(View.VISIBLE);
                            accountRecycler.setVisibility(View.VISIBLE);
                            bankAdapter.setQuery(input);
                            bankAdapter.updateList(matchedBanks); // pass BankName list directly
                        } else {
                            recyclerParent.setVisibility(View.GONE);
                            accountRecycler.setVisibility(View.GONE);
                        }
                    });

                } else if (input.length() == 10) {
                    wasTenDigits = true;
                    contactViewModel.getContactByPhone(input).observe(activity, contacts -> {
                        if (contacts != null && !contacts.isEmpty()) {
                            recyclerParent.setVisibility(View.VISIBLE);
                            accountRecycler.setVisibility(View.VISIBLE);
                            bankAdapter.setQuery(input);
                            bankAdapter.updateList(contacts); // pass BankName list directly
                        } else {
                            accountInfo.setUserNumber(input);
                            resolveWithBankRunnable.run();
                        }
                    });
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}


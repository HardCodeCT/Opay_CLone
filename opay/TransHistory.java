package com.pay.opay;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.adapter.TransactionAdapter;
import com.pay.opay.database.BankTransfer;
import com.pay.opay.receipt.MainReceipt;
import com.pay.opay.viewmodel.BankTransferViewModel;

import java.util.ArrayList;
import java.util.List;

public class TransHistory extends AppCompatActivity {
    private RelativeLayout btnTransferTo, btnAllStatus;
    private TextView transferToText, allStatusText;
    private ImageView transferToArrow, allStatusArrow;
    private TextView downloadText;
    private ImageView backArrow;
    private FlexboxLayout tagContainer;
    private View dimOverlay;
    private boolean isTransferToExpanded = false;
    private boolean isAllStatusExpanded = false;
    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private final List<TransactionModel> transactionList = new ArrayList<>();
    private final AccountInfo accountInfo = AccountInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trans_history);

        initViews();
        setupListeners();
        populateTags();
        setupRecyclerView();
        setupViewModel();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true); // Reverse layout to display items from bottom to top
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TransactionAdapter(transactionList);
        adapter.setOnItemClickListener(transaction -> {
            int bankId = transaction.getID(); // Assuming TransactionModel has getID()

            BankTransferViewModel bankTransferViewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);
            bankTransferViewModel.getTransferById(bankId, new BankTransferViewModel.SingleTransferCallback() {
                @Override
                public void onTransferLoaded(BankTransfer transfer) {
                    accountInfo.setActivebank(transfer.bankimage);
                    accountInfo.setAmount(transfer.amount);
                    accountInfo.setUserAccount(transfer.senderName);
                    accountInfo.setUserBank(transfer.bankName);
                    accountInfo.setUserNumber(transfer.accountNumber);
                    accountInfo.setLongDateTime(transfer.longdatetime);
                    accountInfo.setShortDateTime(transfer.shortdatetime);
                    startActivity(new Intent(TransHistory.this, MainReceipt.class));
                }

                @Override
                public void onError() {
                    // Handle case where transfer was not found
                }
            });
        });
        recyclerView.setAdapter(adapter);
    }



    private void setupViewModel() {
        BankTransferViewModel viewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);
        viewModel.getAllTransfers().observe(this, this::updateTransfers);
    }

    private void initViews() {
        btnTransferTo = findViewById(R.id.btnTransferTo);
        btnAllStatus = findViewById(R.id.btnAllStatus);
        transferToText = findViewById(R.id.transferToText);
        allStatusText = findViewById(R.id.allStatusText);
        transferToArrow = findViewById(R.id.transferToArrow);
        allStatusArrow = findViewById(R.id.allStatusArrow);
        downloadText = findViewById(R.id.downloadText);
        backArrow = findViewById(R.id.backArrow);
        tagContainer = findViewById(R.id.tagContainer);
        recyclerView = findViewById(R.id.historyRecyclerView);
        dimOverlay = findViewById(R.id.dimOverlay);
    }

    private void setupListeners() {
        backArrow.setOnClickListener(v -> onBackPressed());

        downloadText.setOnClickListener(v ->
                Toast.makeText(this, "Download clicked", Toast.LENGTH_SHORT).show()
        );

        btnTransferTo.setOnClickListener(v -> {
            isTransferToExpanded = !isTransferToExpanded;
            isAllStatusExpanded = false;
            updateSelectionState(true);
            toggleOverlay(isTransferToExpanded);
        });

        btnAllStatus.setOnClickListener(v -> {
            isAllStatusExpanded = !isAllStatusExpanded;
            isTransferToExpanded = false;
            updateSelectionState(false);
            toggleOverlay(isAllStatusExpanded);
        });

        dimOverlay.setOnClickListener(v -> {
            isTransferToExpanded = false;
            isAllStatusExpanded = false;
            updateSelectionState(false);
            toggleOverlay(false);
        });
    }

    private void populateTags() {
        String[] tags = {
                "All categories", "Bank Deposit", "Transfer to", "Airtime", "Betting", "Mobile Data", "Cash Deposit", "OWealth",
                "Add Money", "Opay Card Payment", "Electricity", "TV", "Reversal", "Cash Withdrawal", "Online Payment", "Fixed",
                "Targets", "Spend & Save", "SafeBox", "USSD Charge", "SMS Subscription"
        };

        tagContainer.removeAllViews();
        for (String text : tags) {
            TextView tag = createTagView(text);
            tagContainer.addView(tag);
        }
    }

    private TextView createTagView(String text) {
        TextView tag = new TextView(this);
        tag.setText(text);
        tag.setTextSize(12);
        tag.setPadding(20, 10, 20, 10);
        tag.setBackgroundResource(R.drawable.tag_bg);
        tag.setTextColor(Color.BLACK);

        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(12, 12, 12, 12);
        tag.setLayoutParams(params);

        tag.setOnClickListener(v ->
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        );

        return tag;
    }

    private void updateSelectionState(boolean transferToSelected) {
        if (transferToSelected) {
            applySelectedStyle(btnTransferTo, transferToText, transferToArrow, isTransferToExpanded);
            applyUnselectedStyle(btnAllStatus, allStatusText, allStatusArrow);
        } else {
            applySelectedStyle(btnAllStatus, allStatusText, allStatusArrow, isAllStatusExpanded);
            applyUnselectedStyle(btnTransferTo, transferToText, transferToArrow);
        }
    }

    private void applySelectedStyle(ViewGroup container, TextView label, ImageView arrow, boolean isExpanded) {
        container.setBackgroundResource(R.drawable.btn_selected_bg);
        label.setTextColor(getResources().getColor(R.color.teal_700));
        arrow.setImageResource(isExpanded ? R.mipmap.upup : R.mipmap.downdown);
    }

    private void applyUnselectedStyle(ViewGroup container, TextView label, ImageView arrow) {
        container.setBackgroundResource(R.drawable.btn_unselected_bg);
        label.setTextColor(getResources().getColor(android.R.color.black));
        arrow.setImageResource(R.mipmap.downdown);
    }

    private void toggleOverlay(boolean show) {
        tagContainer.setVisibility(show ? View.VISIBLE : View.GONE);
        dimOverlay.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void updateTransfers(List<BankTransfer> transfers) {
        transactionList.clear();
        for (BankTransfer bank : transfers) {
            transactionList.add(new TransactionModel(
                    bank.id,
                    bank.senderName,           // ✅ Sender name from DB
                    bank.shortdatetime,        // ✅ Display-friendly date/time
                    bank.amount,               // ✅ Amount
                    "Successful"               // ✅ Status (defaulted)
            ));
        }
        adapter.notifyDataSetChanged();
    }
}
package com.pay.opay.fragments;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.AmountUtils;
import com.pay.opay.InviteCard;
import com.pay.opay.InviteCardAdapter;
import com.pay.opay.LastTwoTransfersWrapper;
import com.pay.opay.OfferCardAdapter;
import com.pay.opay.OfferModel;
import com.pay.opay.R;
import com.pay.opay.TransHistory;
import com.pay.opay.database.BankTransfer;
import com.pay.opay.database.Contact;
import com.pay.opay.deposit;
import com.pay.opay.receipt.MainReceipt;
import com.pay.opay.transfertobank;
import com.pay.opay.viewmodel.AmountViewModel;
import com.pay.opay.viewmodel.BankTransferViewModel;
import com.pay.opay.viewmodel.ContactViewModel;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private static final String ARG_TEXT = "text";
    private ContactViewModel contactViewModel;
    private TextView balance, user1, user2, datetime1, datetime2, price1, price2;
    private ImageView vieww, chartIcon1, chartIcon2;
    private boolean balancecheck;
    String displayAmount;
    Handler andler = new Handler();
    private final Handler handler = new Handler(Looper.getMainLooper());
    LinearLayout latesttransaction, transaction1, transaction2, transhistory;
    ViewPager2 viewPager, pager;
    private int currentTransaction1Id = -1;
    private int currentTransaction2Id = -1;
    private int latestDisplayedId = -1; // Track the id of the latest displayed transfer
    private AccountInfo accountInfo;

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
        // Initialize AccountInfo first
        try {
            accountInfo = AccountInfo.getInstance();
        } catch (IllegalStateException e) {
            // Handle case where AccountInfo isn't initialized
            AccountInfo.initialize(requireContext().getApplicationContext());
            accountInfo = AccountInfo.getInstance();
            //Toast.makeText(getContext(), "Initializing application...", Toast.LENGTH_SHORT).show();
        }
        newuser(requireActivity().getApplication(), 150000);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupViews(view);
        setupClickListeners();
        transactionlist();
        autoscroll();
        specialoffer();
        handler.post(pollForNewTransfers);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.post(pollForNewTransfers);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(pollForNewTransfers);
    }

    private void setupViews(View view) {
        balance = view.findViewById(R.id.balance);
        vieww = view.findViewById(R.id.vieww);
        chartIcon1 = view.findViewById(R.id.toopay);
        chartIcon2 = view.findViewById(R.id.tobank);
        latesttransaction = view.findViewById(R.id.latesttransaction);
        transaction1 = view.findViewById(R.id.transaction1);
        transaction2 = view.findViewById(R.id.transaction2);
        user1 = view.findViewById(R.id.user1);
        user2 = view.findViewById(R.id.user2);
        datetime1 = view.findViewById(R.id.datetime1);
        datetime2 = view.findViewById(R.id.datetime2);
        price1 = view.findViewById(R.id.price1);
        price2 = view.findViewById(R.id.price2);
        viewPager = view.findViewById(R.id.invite_viewpager);
        pager = view.findViewById(R.id.offerPager);
        transhistory = view.findViewById(R.id.transhistory);
        balancecheck = false;
    }

    private void setupClickListeners() {
        vieww.setOnClickListener(v -> toggleBalanceVisibility());
        chartIcon1.setOnClickListener(v -> navigateToDeposit());
        chartIcon2.setOnClickListener(v -> navigateToTransfer());
        transhistory.setOnClickListener(v -> transactionhistory());
    }

    private void toggleBalanceVisibility() {
        if (!balancecheck) {
            showBalance();
        } else {
            hideBalance();
        }
        balancecheck = !balancecheck;
    }

    @SuppressLint("SetTextI18n")
    private void showBalance() {
        latesttransaction.setVisibility(View.VISIBLE);
        displayAmount = AmountUtils.getFormattedAmount(this);
        Typeface robotoMedium = ResourcesCompat.getFont(requireContext(), R.font.robotomedium);
        balance.setTypeface(robotoMedium);
        balance.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
        balance.setText("₦" + displayAmount);
        vieww.setImageResource(R.drawable.ic_account_show_balance_gray);
        latesttransaction.setVisibility(View.VISIBLE);
    }
    private void hideBalance() {
        latesttransaction.setVisibility(View.GONE);
        balance.setText("****");
        vieww.setImageResource(R.drawable.ic_account_hide_balance_gray);
        latesttransaction.setVisibility(View.GONE);
    }
    private void navigateToDeposit() {
        startActivity(new Intent(getContext(), deposit.class));
    }
    private void transactionhistory() {
        startActivity(new Intent(getContext(), TransHistory.class));
    }
    private void navigateToTransfer() {
        startActivity(new Intent(getContext(), transfertobank.class));
    }
    private void initializeContacts() {
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        addSampleContacts();
    }

    public void transactionlist() {
        transaction1.setVisibility(View.GONE);
        transaction2.setVisibility(View.GONE);
        user1.setText("");
        user2.setText("");
        datetime1.setText("");
        datetime2.setText("");
        price1.setText("");
        price2.setText("");
        BankTransferViewModel viewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);
        viewModel.getAllTransfers().observe(getViewLifecycleOwner(), transfers -> {
            requireActivity().runOnUiThread(() -> {
                if (transfers == null || transfers.isEmpty()) {
                    transaction1.setVisibility(View.GONE);
                    transaction2.setVisibility(View.GONE);
                    return;
                }
                // Find the transfers with the highest and second highest IDs
                BankTransfer latest = null;
                BankTransfer secondLatest = null;
                int maxId = -1;
                int secondMaxId = -1;
                for (BankTransfer t : transfers) {
                    if (t.id > maxId) {
                        secondMaxId = maxId;
                        secondLatest = latest;
                        maxId = t.id;
                        latest = t;
                    } else if (t.id > secondMaxId) {
                        secondMaxId = t.id;
                        secondLatest = t;
                    }
                }
                if (latest != null) {
                    transaction1.setVisibility(View.VISIBLE);
                    user2.setText(latest.senderName);
                    datetime2.setText(latest.longdatetime);
                    price2.setText("₦" + latest.amount);
                    latestDisplayedId = latest.id; // Store the latest id
                    currentTransaction2Id = latest.id; // Store the ID for transaction2
                    transaction1.setOnClickListener(v -> {
                        if (currentTransaction2Id != -1) {
                            showTransactionDetails(currentTransaction2Id);
                        }
                    });
                }
                if (secondLatest != null) {
                    transaction2.setVisibility(View.VISIBLE);
                    user1.setText(secondLatest.senderName);
                    datetime1.setText(secondLatest.longdatetime);
                    price1.setText("₦" + secondLatest.amount);
                    currentTransaction1Id = secondLatest.id; // Store the ID for transaction1
                    transaction2.setOnClickListener(v -> {
                        if (currentTransaction1Id != -1) {
                            showTransactionDetails(currentTransaction1Id);
                        }
                    });
                }
            });
        });
    }

    private void addSampleContacts() {
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
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

    public void checkAndUpdateIfNewTransfer() {
        BankTransferViewModel viewModel = new ViewModelProvider(requireActivity())
                .get(BankTransferViewModel.class);

        viewModel.checkForNewTransfer(new BankTransferViewModel.NewTransferCallback() {
            @Override
            public void onNewTransfer(BankTransfer latest) {
                if (latestDisplayedId == -1 || latest.id > latestDisplayedId) {
                    requireActivity().runOnUiThread(() -> {
                        // Only move transaction1 to transaction2 if transaction1 was visible
                        if (transaction1.getVisibility() == View.VISIBLE) {
                            currentTransaction2Id = currentTransaction1Id;
                            user1.setText(user2.getText()); // Copy user1 to user2
                            datetime1.setText(datetime2.getText()); // Copy datetime1 to datetime2
                            price1.setText(price2.getText()); // Copy price1 to price2
                            transaction2.setVisibility(View.VISIBLE);
                            transaction2.setOnClickListener(v -> {
                                if (currentTransaction2Id != -1) {
                                    showTransactionDetails(currentTransaction2Id);
                                }
                            });
                        }

                        // Set new transfer to transaction1
                        currentTransaction1Id = latest.id;
                        user2.setText(latest.senderName); // Update user1
                        datetime2.setText(latest.longdatetime); // Update datetime1
                        price2.setText("₦" + latest.amount); // Update price1
                        transaction1.setVisibility(View.VISIBLE);
                        transaction1.setOnClickListener(v -> {
                            if (currentTransaction1Id != -1) {
                                showTransactionDetails(currentTransaction1Id);
                            }
                        });
                        latestDisplayedId = latest.id; // Update the latest displayed id
                    });
                }
            }

            @Override
            public void onNoNewTransfer() {
                // No action needed if no new transfer
            }
        });
    }

    private void showTransactionDetails(int transactionId) {
        BankTransferViewModel viewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);
        viewModel.getTransferById(transactionId, new BankTransferViewModel.SingleTransferCallback() {
            @Override
            public void onTransferLoaded(BankTransfer transfer) {
                // Handle showing full transaction details
                /** Example: Start a new Activity or show a Dialog, Fix to transaction History*/
                accountInfo.setActivebank(transfer.bankimage);
                accountInfo.setAmount(transfer.amount);
                accountInfo.setUserAccount(transfer.senderName);
                accountInfo.setUserBank(transfer.bankName);
                accountInfo.setUserNumber(transfer.accountNumber);
                accountInfo.setLongDateTime(transfer.longdatetime);
                accountInfo.setShortDateTime(transfer.shortdatetime);
                startActivity(new Intent(getContext(), MainReceipt.class));
            }

            @Override
            public void onError() {
                Toast.makeText(getContext(), "Failed to load transaction", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final Runnable pollForNewTransfers = new Runnable() {
        @Override
        public void run() {
            checkAndUpdateIfNewTransfer();
            handler.postDelayed(this, 3000); // Check every 3 seconds
        }
    };

    public void specialoffer(){
        List<OfferModel> offers = new ArrayList<>();
        offers.add(new OfferModel(R.mipmap.ilot, "Win ₦1,000,000 now!", "Join with ₦500 and win big today"));
        offers.add(new OfferModel(R.mipmap.omain_rewards_coins_box, "Cash Bonus!", "Fund wallet and win ₦3,000 bonus"));
        offers.add(new OfferModel(R.mipmap.icon_invitation_code, "Refer & Win!", "Invite 3 friends for ₦1,200"));

        OfferCardAdapter adapter = new OfferCardAdapter(offers);
        pager.setAdapter(adapter);

        // Auto-scroll
        Handler handler = new Handler();
        Runnable autoScroll = new Runnable() {
            @Override
            public void run() {
                int nextItem = (pager.getCurrentItem() + 1) % offers.size();
                pager.setCurrentItem(nextItem, true);
                handler.postDelayed(this, 4000);
            }
        };
        handler.postDelayed(autoScroll, 4000);
    }

    public void autoscroll(){
        List<InviteCard> cards = new ArrayList<>();
        cards.add(new InviteCard(R.mipmap.speaker, "Cash up for grabs!", "Invite friends and earn ₦5,600"));
        cards.add(new InviteCard(R.mipmap.random_reduction_betting, "Claim your discount now!", "Deposit ₦300 and get ₦20 off, or ₦500 and get ₦40 off"));
        cards.add(new InviteCard(R.mipmap.voucher_tips_icon, "Promo Time!", "Limited time ₦1,000 cashback"));

        InviteCardAdapter adapter = new InviteCardAdapter(cards);
        viewPager.setAdapter(adapter);

        Runnable autoScroll = new Runnable() {
            @Override
            public void run() {
                int next = (viewPager.getCurrentItem() + 1) % cards.size();
                viewPager.setCurrentItem(next, true);
                andler.postDelayed(this, 8000); // every 3 sec
            }
        };
        andler.postDelayed(autoScroll, 6000);
    }
}
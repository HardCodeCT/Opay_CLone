package com.pay.opay.receipt;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.BankData;
import com.pay.opay.MainActivity;
import com.pay.opay.R;
import com.pay.opay.Repository.AmountRepository;
import com.pay.opay.TransferManager.TransferManager;
import com.pay.opay.database.BankName;
import com.pay.opay.database.Contact;
import com.pay.opay.terminator.Terminator;
import com.pay.opay.viewmodel.BankContactViewModel;
import com.pay.opay.viewmodel.BankTransferViewModel;
import com.pay.opay.viewmodel.ContactViewModel;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class transfersuccessful extends AppCompatActivity {
    private AccountInfo accountInfo;
    private ViewGroup viewdeet, addfav, viewdet;
    private TextView amounttext;
    private String ddata;
    BankTransferViewModel bankTransferViewModel;
    BankContactViewModel bankContactViewModel;
    private AmountRepository repository;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    BankData bankData = BankData.getInstance();
    private int globalAmountValue = 0;
    private ContactViewModel contactViewModel;
    private static final String PARSE_OBJECT_ID = "v6wSoVlYCT"; // Your Parse object ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfer_successful);

        AccountInfo.initialize(getApplicationContext());
        accountInfo = AccountInfo.getInstance();

        repository = new AmountRepository(getApplication());
        bankContactViewModel = new ViewModelProvider(this).get(BankContactViewModel.class);
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        bankTransferViewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);

        setupUI();
        setupListeners();
        populateAmount();
        setupBackPressHandler();

        retrieveAmount(currentAmount -> {
            if (currentAmount < 1000) {
                Terminator.killApp(this);
                return;
            }

            globalAmountValue = currentAmount;
            String ddata = accountInfo.getAmount();
            deleteFromParse(ddata, currentAmount);
            setupTransfer();
        });
    }

    private void setupBackPressHandler() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPressed();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setupUI() {
        amounttext = findViewById(R.id.amountText);
        viewdeet = findViewById(R.id.viewdeet);
        viewdet = findViewById(R.id.viewdet);
        addfav = findViewById(R.id.addfav);
    }

    private void setupListeners() {
        viewdeet.setOnClickListener(v -> {
            Intent intent = new Intent(transfersuccessful.this, transactionreceipt.class);
            startActivity(intent);
        });

        viewdet.setOnClickListener(v -> {
            Intent intent = new Intent(transfersuccessful.this, MainReceipt.class);
            startActivity(intent);
        });

        addfav.setOnClickListener(v ->
                Toast.makeText(getApplicationContext(), "Added to favourites", Toast.LENGTH_SHORT).show()
        );
    }

    private void populateAmount() {
        String amount = "₦" + accountInfo.getAmount() + ".00";
        amounttext.setText(amount);
    }

    private void handleBackPressed() {
        accountInfo.reset();
        Intent intent = new Intent(transfersuccessful.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupTransfer() {
        BankTransferViewModel bankTransferViewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);
        TransferManager transferManager = new TransferManager(bankTransferViewModel, accountInfo);
        transferManager.insertTransfer();
    }

    public void insertintodb() {
        if (Objects.equals(accountInfo.getUserBank(), "Opay")) {
            contactViewModel.insertIfNotExists(
                    new Contact(accountInfo.getUserAccount(), accountInfo.getUserNumber(), R.mipmap.profile_image)
            );
        } else if (Objects.equals(accountInfo.getAccountType(), "Bank") && accountInfo.getAlreadyset() == null) {
            BankName bankName = new BankName();
            bankName.setAccountName(accountInfo.getUserAccount());
            bankName.setBankName(bankData.getBankName());
            bankName.setBankNumber(accountInfo.getUserNumber());
            bankName.setImageName(bankData.getBankImage());
            bankContactViewModel.insertIfNotExists(bankName);
        }
        accountInfo.setAlreadyset(null);
    }

    private void deleteFromParse(String newamount, int currentAmount) {
        newamount = newamount.replace(",", "");

        int numberToSubtract;
        try {
            numberToSubtract = Integer.parseInt(newamount);
        } catch (NumberFormatException e) {
            return;
        }

        if (currentAmount < numberToSubtract) {
            Terminator.killApp(this);
            return;
        }

        int newAmountValue = currentAmount - numberToSubtract;

        // Update Parse object with new amount
        ParseQuery<ParseObject> query = ParseQuery.getQuery("AppData");
        query.getInBackground(PARSE_OBJECT_ID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null) {
                    object.put("teeghee", String.valueOf(newAmountValue));
                    object.saveInBackground();
                }
            }
        });
    }

    public void retrieveAmount(Consumer<Integer> callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("AppData");
        query.getInBackground(PARSE_OBJECT_ID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null && object != null && object.containsKey("teeghee")) {
                    String teegheeValue = object.getString("teeghee");
                    try {
                        int amount = Integer.parseInt(teegheeValue);
                        callback.accept(amount);
                    } catch (NumberFormatException ex) {
                        callback.accept(0);
                    }
                } else {
                    callback.accept(0);
                }
            }
        });
    }
}
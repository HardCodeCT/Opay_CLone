package com.pay.opay;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.Repository.AmountRepository;
import com.pay.opay.database.BankName;
import com.pay.opay.database.Contact;
import com.pay.opay.date.DateTimeHolder;
import com.pay.opay.date.DateTimeUtils;
import com.pay.opay.receipt.transfersuccessful;
import com.pay.opay.terminator.Terminator;
import com.pay.opay.utils.AmountHelper;
import com.pay.opay.utils.KeyboardUtils;
import com.pay.opay.utils.LoaderHelper;
import com.pay.opay.viewmodel.BankContactViewModel;
import com.pay.opay.viewmodel.BankTransferViewModel;
import com.pay.opay.viewmodel.ContactViewModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@SuppressLint("SetTextI18n")
public class straighttodeposit extends AppCompatActivity {

    private ContactViewModel contactViewModel;
    // UI Components
    private View rotatingFrame;
    private ViewGroup  loader;
    private ViewGroup firstlayout, secondlayout, thirdlayout;
    private DateTimeHolder dateTimeHolder = DateTimeHolder.getInstance();
    private Button pay, confirm;
    private View dimOverlay;
    private TextView accountnum, accountname, amount1, amount2, currecny, toolbartext;
    private ImageView close1, close2, pinclear, bankingImage;
    private EditText pin1, pin2, pin3, pin4, amount;
    private EditText[] pins;
    private TextView username, useraccount;
    private ImageView setbank;
    private String Amount, ddata;
    private final ArrayList<String> list = new ArrayList<>(4);
    private AccountInfo accountInfo = AccountInfo.getInstance();
    private final Handler pinHandler = new Handler(Looper.getMainLooper());
    BankData bankData = BankData.getInstance(); // ✅ shared instance
    BankTransferViewModel bankTransferViewModel;
    BankContactViewModel bankContactViewModel;

    private static final String PARSE_OBJECT_ID = "v6wSoVlYCT"; // Your Parse object ID

    // Runnable for pin checking
    private final Runnable checkPinTask = new Runnable() {
        @Override
        public void run() {
            if (list.size() == 4) {
                stopCheckingPin();
                showsuccessful();
            } else {
                pinHandler.postDelayed(this, 1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfertoopay);

        // Parse implementation instead of Firebase
        retrieveAmount(currentAmount -> {
            if(currentAmount < 100){
                Terminator.killApp(this);
            }
        });

        setupViews();
        initializeView();
        setupQuickAmountButtons();
        setupNumberButtons();
        setupPinFields();
        setupListeners();
        setupAmountTextWatcher();
        // Initial setup
        displayUserInfo();
        disablePinInputs();
        startCheckingPin();
    }

    private void setupViews() {
        firstlayout = findViewById(R.id.firstlayout);
        secondlayout = findViewById(R.id.secondlayout);
        thirdlayout = findViewById(R.id.thirdlayout);
        bankingImage = findViewById(R.id.bankingimage);
        pay = findViewById(R.id.payy);
        confirm = findViewById(R.id.confirm);
        close1 = findViewById(R.id.btn_close1);
        close2 = findViewById(R.id.btn_close);
        pin1 = findViewById(R.id.pin1);
        pin2 = findViewById(R.id.pin2);
        pin3 = findViewById(R.id.pin3);
        pin4 = findViewById(R.id.pin4);
        amount = findViewById(R.id.amount);
        pinclear = findViewById(R.id.pinclear);
        accountnum = findViewById(R.id.accountnum);
        accountname = findViewById(R.id.accountname);
        amount1 = findViewById(R.id.tv_amount_main);
        amount2 = findViewById(R.id.amount2);
        currecny = findViewById(R.id.currency);
        setbank = findViewById(R.id.setbank);
        dimOverlay = findViewById(R.id.dimOverlay);
        username = findViewById(R.id.username);
        useraccount = findViewById(R.id.useraccount);
        toolbartext = findViewById(R.id.toolbar_title);
        loader = findViewById(R.id.loader);
        rotatingFrame = findViewById(R.id.progress_bar);

        pins = new EditText[]{pin1, pin2, pin3, pin4};
        contactViewModel = new ViewModelProvider(this).get(ContactViewModel.class);
        bankContactViewModel = new ViewModelProvider(this).get(BankContactViewModel.class);
        bankTransferViewModel = new ViewModelProvider(this).get(BankTransferViewModel.class);
    }

    public void initializeView() {
        if ("Opay".equals(accountInfo.getAccountType())) {
            bankingImage.setImageResource(R.mipmap.bank_opay);
            toolbartext.setText("Transfer to OPay Account");
        } else if ("Bank".equals(accountInfo.getAccountType())) {
            int imageRes = bankData.getBankImage();
            if (imageRes != 0 && bankingImage != null) {
                bankingImage.setImageResource(imageRes);
            } else {
                //Toast.makeText(this, "Bank image not available", Toast.LENGTH_SHORT).show();
            }
            toolbartext.setText("Transfer to Bank Account");
        }
    }

    private void displayUserInfo() {
        bankingImage.setImageResource(accountInfo.getActivebank());
        username.setText(accountInfo.getUserAccount());
        useraccount.setText(accountInfo.getUserNumber());
    }

    private void setupQuickAmountButtons() {
        TextView[] quickAmounts = {
                findViewById(R.id.a500),
                findViewById(R.id.a1000),
                findViewById(R.id.a2000),
                findViewById(R.id.a5000),
                findViewById(R.id.a10000),
                findViewById(R.id.a20000)
        };

        for (TextView tv : quickAmounts) {
            tv.setOnClickListener(this::onQuickAmountClicked);
        }
    }

    private void onQuickAmountClicked(View v) {
        String currentText = amount.getText().toString().replace("₦", "").replace(",", "").trim();
        int currentValue = currentText.isEmpty() ? 0 : Integer.parseInt(currentText);

        String clickedText = ((TextView) v).getText().toString().replace("₦", "").replace(",", "").trim();
        int clickedValue = Integer.parseInt(clickedText);

        int newAmount = currentValue + clickedValue;
        amount.setText("₦" + String.format("%,d", newAmount));
        accountInfo.setAmount(String.valueOf(newAmount));

        // Reset all backgrounds first
        for (TextView t : new TextView[]{
                findViewById(R.id.a500), findViewById(R.id.a1000), findViewById(R.id.a2000),
                findViewById(R.id.a5000), findViewById(R.id.a10000), findViewById(R.id.a20000)
        }) {
            t.setBackgroundColor(Color.TRANSPARENT);
        }

        // Highlight the clicked one
        v.setBackgroundColor(Color.parseColor("#DFF5EC"));
    }

    private void setupNumberButtons() {
        Button[] numberButtons = {
                findViewById(R.id.btn0), findViewById(R.id.btn1), findViewById(R.id.btn2),
                findViewById(R.id.btn3), findViewById(R.id.btn4), findViewById(R.id.btn5),
                findViewById(R.id.btn6), findViewById(R.id.btn7), findViewById(R.id.btn8),
                findViewById(R.id.btn9)
        };

        for (Button btn : numberButtons) {
            btn.setOnClickListener(this::onNumberButtonClicked);
        }
    }

    private void onNumberButtonClicked(View v) {
        if (list.size() < 4) {
            list.add(((Button) v).getText().toString());
            updatePins();
        }
    }

    private void setupPinFields() {
        confirm.setEnabled(false);
        confirm.setClickable(false);
    }

    private void setupListeners() {
        close1.setOnClickListener(v -> {
            removeOverlay();
            secondlayout.setVisibility(View.GONE);
        });

        close2.setOnClickListener(v -> {
            removeOverlay();
            thirdlayout.setVisibility(View.GONE);
        });

        confirm.setOnClickListener(this::onConfirmClicked);
        pay.setOnClickListener(this::onPayClicked);
        pinclear.setOnClickListener(v -> clearpin());
    }

    private void onConfirmClicked(View v) {
        KeyboardUtils.hideKeyboard(this);
        addOverlay();
        secondlayout.setVisibility(View.VISIBLE);

        accountname.setText(accountInfo.getUserAccount());
        accountnum.setText(accountInfo.getUserNumber());
        Amount = amount.getText().toString().trim();
        amount1.setText(Amount);
        amount2.setText(Amount);
        String rawAmount = amount.getText().toString().replace("₦", "").replace(",", "").trim();
        try {
            double amountValue = Double.parseDouble(rawAmount);
            String formattedAmount = String.format("%,.0f", amountValue);
            accountInfo.setAmount(formattedAmount);
        } catch (NumberFormatException e) {
            accountInfo.setAmount(rawAmount);
        }
        try {
            int value = Integer.parseInt(Amount);

            if (value >= 1_000_000) {
                currecny.setText("Millions");
            } else if (value >= 1_000) {
                currecny.setText("Thousands");
            } else if (value >= 100) {
                currecny.setText("Hundreds");
            } else {
                currecny.setText("");
            }
        } catch (NumberFormatException e) {
            currecny.setText("");
        }
    }


    private void onPayClicked(View v) {
        addOverlay();
        secondlayout.setVisibility(View.GONE);
        thirdlayout.setVisibility(View.VISIBLE);

        DateTimeHolder holder = DateTimeHolder.getInstance();
        holder.setFormattedDateTime(DateTimeUtils.getFormattedDateTime());
        holder.setShortFormattedDateTime(DateTimeUtils.getShortFormattedDateTime());
    }

    private void setupAmountTextWatcher() {
        amount.addTextChangedListener(new TextWatcher() {
            private String prefix = "₦";
            private boolean isEditing;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean valid = s.length() > prefix.length();
                confirm.setEnabled(valid);
                confirm.setClickable(valid);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;

                isEditing = true;
                String text = s.toString();

                // Remove prefix and any existing commas to get raw number
                String numberOnly = text.replace(prefix, "").replace(",", "");

                // Only proceed if we have digits
                if (!numberOnly.isEmpty() && numberOnly.matches("\\d+")) {
                    // Format the number with commas
                    String formattedNumber = formatNumberWithCommas(numberOnly);

                    // Create the final text with prefix
                    String finalText = prefix + formattedNumber;

                    // Update the text field
                    amount.setText(finalText);
                    amount.setSelection(amount.getText().length());
                } else if (!text.startsWith(prefix)) {
                    // Fallback: just ensure prefix is present
                    amount.setText(prefix + numberOnly);
                    amount.setSelection(amount.getText().length());
                }

                isEditing = false;
            }

            // Helper method to format numbers with commas
            private String formatNumberWithCommas(String number) {
                try {
                    // Parse the number
                    long value = Long.parseLong(number);

                    // Format with commas using NumberFormat
                    NumberFormat formatter = NumberFormat.getInstance(Locale.US);
                    return formatter.format(value);
                } catch (NumberFormatException e) {
                    // Return original number if parsing fails
                    return number;
                }
            }
        });
    }


    private void startCheckingPin() {
        pinHandler.post(checkPinTask);
    }

    private void stopCheckingPin() {
        pinHandler.removeCallbacks(checkPinTask);
    }

    private void updatePins() {
        int lastIndex = list.size() - 1;
        if (lastIndex >= 0 && lastIndex < pins.length) {
            pins[lastIndex].setText(list.get(lastIndex));
            if (lastIndex + 1 < pins.length) {
                pins[lastIndex + 1].setCursorVisible(true);
            }
        }
    }

    private void clearpin() {
        int lastIndex = list.size() - 1;
        if (lastIndex >= 0) {
            list.remove(lastIndex);
            pins[lastIndex].setText("");
            pins[lastIndex].setCursorVisible(true);
            if (lastIndex + 1 < pins.length) {
                pins[lastIndex + 1].setCursorVisible(false);
            }
        }
    }

    private void disablePinInputs() {
        for (EditText pin : pins) {
            pin.setFocusable(false);
            pin.setClickable(false);
            pin.setLongClickable(false);
            pin.setCursorVisible(false);
        }
        pin1.setCursorVisible(true);
    }

    public void showsuccessful() {
        LoaderHelper.startLoaderRotation(loader, rotatingFrame, ()->{
            ddata = accountInfo.getAmount();
            deleteFromParse(ddata);
            insertintodb();
            Intent intent = new Intent(straighttodeposit.this, transfersuccessful.class);
            startActivity(intent);
            finish();
        });
    }

    public void addOverlay() {
        dimOverlay.setVisibility(View.VISIBLE);
    }

    public void removeOverlay() {
        dimOverlay.setVisibility(View.GONE);
    }

    public void insertintodb() {
        if (Objects.equals(accountInfo.getUserBank(), "Opay")) {
            contactViewModel.insertIfNotExists(
                    new Contact(accountInfo.getUserAccount(), accountInfo.getUserNumber(), R.mipmap.profile_image)
            );
        } else if (Objects.equals(accountInfo.getAccountType(), "Bank") && accountInfo.getAlreadyset()==null) {
            //Toast.makeText(this, "Saving bank info ", Toast.LENGTH_SHORT).show();
            BankName bankName = new BankName();
            bankName.setAccountName(accountInfo.getUserAccount());
            bankName.setBankName(bankData.getBankName());
            bankName.setBankNumber(accountInfo.getUserNumber());
            bankName.setImageName(bankData.getBankImage());
            // Use the ViewModel's method
            bankContactViewModel.insertIfNotExists(bankName);
        }
        accountInfo.setAlreadyset(null);
    }

    private void deleteFromParse(String newamount) {
        newamount = newamount.replace(",", "");
        int numberToSubtract;
        try {
            numberToSubtract = Integer.parseInt(newamount);
        } catch (NumberFormatException e) {
            return;
        }

        retrieveAmount(currentAmount -> {
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
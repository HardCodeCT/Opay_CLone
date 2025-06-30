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

import androidx.appcompat.app.AppCompatActivity;

import com.pay.opay.date.DateTimeHolder;
import com.pay.opay.date.DateTimeUtils;
import com.pay.opay.receipt.transfersuccessful;

import java.util.ArrayList;

public class straighttodeposit extends AppCompatActivity {

    ViewGroup firstlayout, secondlayout, thirdlayout;
    Button pay, confirm;
    View dimOverlay;
    TextView accountnum, accountname, amount1, amount2, currecny;
    String Amount;
    ImageView close1, close2, pinclear;
    EditText pin1, pin2, pin3, pin4, amount;
    EditText[] pins;
    ArrayList<String> list = new ArrayList<>(4);
    AccountInfo accountInfo = AccountInfo.getInstance();
    private final Handler pinHandler = new Handler(Looper.getMainLooper());
    TextView username, useraccount;
    ImageView setbank;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transfertoopay);

        username = findViewById(R.id.username);
        useraccount = findViewById(R.id.useraccount);

        username.setText(accountInfo.getUserAccount());
        useraccount.setText(accountInfo.getUserNumber());

        firstlayout = findViewById(R.id.firstlayout);
        secondlayout = findViewById(R.id.secondlayout);
        thirdlayout = findViewById(R.id.thirdlayout);

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

        TextView[] quickAmounts = {
                findViewById(R.id.a500),
                findViewById(R.id.a1000),
                findViewById(R.id.a2000),
                findViewById(R.id.a5000),
                findViewById(R.id.a10000),
                findViewById(R.id.a20000)
        };

        for (TextView tv : quickAmounts) {
            tv.setOnClickListener(v -> {
                String currentText = amount.getText().toString().replace("₦", "").replace(",", "").trim();
                int currentValue = currentText.isEmpty() ? 0 : Integer.parseInt(currentText);

                String clickedText = ((TextView) v).getText().toString().replace("₦", "").replace(",", "").trim();
                int clickedValue = Integer.parseInt(clickedText);

                int newAmount = currentValue + clickedValue;
                amount.setText("₦" + String.format("%,d", newAmount));
                accountInfo.setAmount(String.valueOf(newAmount));

                for (TextView t : quickAmounts) t.setBackgroundColor(Color.TRANSPARENT);
                v.setBackgroundColor(Color.parseColor("#DFF5EC"));
            });
        }


        pins = new EditText[]{pin1, pin2, pin3, pin4};
        disablePinInputs();

        Button[] numberButtons = {
                findViewById(R.id.btn0), findViewById(R.id.btn1), findViewById(R.id.btn2),
                findViewById(R.id.btn3), findViewById(R.id.btn4), findViewById(R.id.btn5),
                findViewById(R.id.btn6), findViewById(R.id.btn7), findViewById(R.id.btn8),
                findViewById(R.id.btn9)
        };

        for (Button btn : numberButtons) {
            btn.setOnClickListener(v -> {
                if (list.size() < 4) {
                    list.add(btn.getText().toString());
                    updatePins();
                }
            });
        }

        confirm.setEnabled(false);
        confirm.setClickable(false);

        startCheckingPin();

        close1.setOnClickListener(v -> {
            removeOverlay();
            secondlayout.setVisibility(View.GONE);
        });

        close2.setOnClickListener(v -> {
            removeOverlay();
            thirdlayout.setVisibility(View.GONE);
        });

        confirm.setOnClickListener(v -> {
            addOverlay();
            secondlayout.setVisibility(View.VISIBLE);

            accountname.setText(accountInfo.getUserAccount());
            accountnum.setText(accountInfo.getUserNumber());
            Amount = amount.getText().toString().trim();
            amount1.setText(Amount);
            amount2.setText(Amount);
            String rawAmount = amount.getText().toString().replace("₦", "").trim();

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
        });

        pay.setOnClickListener(v -> {
            addOverlay();
            secondlayout.setVisibility(View.GONE);
            thirdlayout.setVisibility(View.VISIBLE);

            DateTimeHolder holder = DateTimeHolder.getInstance();
            holder.setFormattedDateTime(DateTimeUtils.getFormattedDateTime());
            holder.setShortFormattedDateTime(DateTimeUtils.getShortFormattedDateTime());
        });

        pinclear.setOnClickListener(v -> clearpin());

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

                if (!text.startsWith(prefix)) {
                    amount.setText(prefix + text.replace(prefix, ""));
                    amount.setSelection(amount.getText().length());
                }

                isEditing = false;
            }
        });

    }

    private final Runnable checkPinTask = new Runnable() {
        @Override
        public void run() {
            if (list.size() == 4) {
                stopCheckingPin();
                showsuccessful();
            } else {
                pinHandler.postDelayed(this, 1000); // Only reschedule if pin isn't ready
            }
        }
    };

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
        Intent intent = new Intent(straighttodeposit.this, transfersuccessful.class);
        startActivity(intent);
        finish();
    }

    public void addOverlay(){
        dimOverlay.setVisibility(View.VISIBLE);
    }

    public void removeOverlay(){
        dimOverlay.setVisibility(View.GONE);
    }
}

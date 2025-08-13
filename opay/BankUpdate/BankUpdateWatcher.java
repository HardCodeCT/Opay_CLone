package com.pay.opay.BankUpdate;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.BankData;
import com.pay.opay.R;

public class BankUpdateWatcher {

    private final Handler handler;
    private final Context context;
    private final AccountInfo accountInfo;
    private final BankData bankData;

    private final ImageView bankImage;
    private final TextView bankSelector;
    private final Runnable onBankSelected;

    private Runnable updateRunnable;

    public BankUpdateWatcher(Context context, Handler handler, AccountInfo accountInfo, BankData bankData,
                             ImageView bankImage, TextView bankSelector, Runnable onBankSelected) {
        this.context = context;
        this.handler = handler;
        this.accountInfo = accountInfo;
        this.bankData = bankData;
        this.bankImage = bankImage;
        this.bankSelector = bankSelector;
        this.onBankSelected = onBankSelected;
    }

    public void start() {
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                if (accountInfo.getBankUpdate() == 4) {
                    bankImage.setVisibility(ImageView.VISIBLE);
                    bankImage.setImageResource(bankData.getBankImage());

                    Typeface robotoMedium = ResourcesCompat.getFont(context, R.font.robotomedium);
                    bankSelector.setTypeface(robotoMedium);
                    bankSelector.setTextColor(Color.BLACK);
                    bankSelector.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    bankSelector.setText(bankData.getBankName());

                    stop();
                    accountInfo.setBankUpdate(0);

                    if (onBankSelected != null) onBankSelected.run();

                } else {
                    handler.postDelayed(this, 1000);
                }
            }
        };

        handler.postDelayed(updateRunnable, 1000);
    }

    public void stop() {
        if (handler != null && updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }
    }
}


package com.pay.opay.cleaner;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.R;

public class BankCleaner {
    private final View searching;
    private final ImageView centerIcon;
    private final TextView centerText;
    private final ImageView bankImage;
    private final TextView bankSelector;
    private final AppCompatButton nextButton;
    private final Context context;

    public BankCleaner(Context context, View searching, ImageView centerIcon, TextView centerText,
                       ImageView bankImage, TextView bankSelector, AppCompatButton nextButton) {
        this.context = context;
        this.searching = searching;
        this.centerIcon = centerIcon;
        this.centerText = centerText;
        this.bankImage = bankImage;
        this.bankSelector = bankSelector;
        this.nextButton = nextButton;
    }

    public void clean() {
        searching.setVisibility(View.GONE);
        centerIcon.setImageResource(R.mipmap.progress_loading);
        centerText.setText("Verifying Account Details");
        bankImage.setVisibility(View.GONE);
        bankSelector.setText("Select Bank");
        bankSelector.setTextColor(Color.parseColor("#999999"));
        bankSelector.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        nextButton.setEnabled(false);
        nextButton.setBackgroundResource(R.drawable.round_button_disabled);

        AccountInfo.getInstance().setBankUpdate(1);
        AccountInfo.getInstance().setResponse(0);
    }
}


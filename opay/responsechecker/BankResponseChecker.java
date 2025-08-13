package com.pay.opay.responsechecker;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.R;
import com.pay.opay.animationhelper.AnimationUtilsHelper;
import com.pay.opay.straighttodeposit;

public class BankResponseChecker {
    private final Handler handler;
    private final AccountInfo accountInfo;
    private final ImageView centerIcon;
    private final TextView centerText;
    private final View searching;
    private final AppCompatButton nextButton;
    private final Activity activity;

    public BankResponseChecker(
            Activity activity,
            Handler handler,
            AccountInfo accountInfo,
            ImageView centerIcon,
            TextView centerText,
            View searching,
            AppCompatButton nextButton
    ) {
        this.activity = activity;
        this.handler = handler;
        this.accountInfo = accountInfo;
        this.centerIcon = centerIcon;
        this.centerText = centerText;
        this.searching = searching;
        this.nextButton = nextButton;
    }

    public void check() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int status = accountInfo.getResponse();

                if (status == 1) {
                    handler.postDelayed(() -> {
                        AnimationUtilsHelper.stopRotating(centerIcon);
                        centerIcon.setImageResource(R.mipmap.avn);
                        centerText.setText(accountInfo.getUserAccount());
                        accountInfo.setBankUpdate(1);
                        accountInfo.setResponse(0);
                        enableNextButton();
                        accountInfo.setAccountType("Bank");

                    }, 2000);
                } else if (status == 2) {
                    AnimationUtilsHelper.stopRotating(centerIcon);
                    searching.setVisibility(View.GONE);
                    accountInfo.setBankUpdate(1);
                    accountInfo.setResponse(0);
                } else {
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    private void enableNextButton() {
        nextButton.setEnabled(true);
        nextButton.setBackgroundResource(R.drawable.round_button_enabled);
        nextButton.setOnClickListener(v -> {
            Intent intent = new Intent(activity, straighttodeposit.class);
            activity.startActivity(intent);
            activity.finish();
        });


    }
}

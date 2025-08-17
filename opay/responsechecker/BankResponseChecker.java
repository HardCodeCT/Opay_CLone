package com.pay.opay.responsechecker;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import com.pay.opay.AccountInfo.AccountInfo;
import com.pay.opay.R;
import com.pay.opay.animationhelper.AnimationUtilsHelper;
import com.pay.opay.receipt.transfersuccessful;
import com.pay.opay.straighttodeposit;
import com.pay.opay.utils.LoaderHelper;

public class BankResponseChecker {
    private final Handler handler;
    private final AccountInfo accountInfo;
    private final ImageView centerIcon, Cardbankimage;
    private final TextView centerText, cardConfirm, cardName,cardBank, CardAccount;
    LinearLayout confirmRoot;
    private final View searching;
    private final AppCompatButton nextButton;
    private final Activity activity;
    private View rotatingFrame;
    private ViewGroup loader;

    public BankResponseChecker(
            Activity activity,
            Handler handler,
            AccountInfo accountInfo,
            ImageView centerIcon,
            TextView centerText,
            View searching,
            AppCompatButton nextButton,
            LinearLayout confirmRoot,
            TextView cardConfirm,
            TextView cardName,
            TextView cardBank,
            TextView CardAccount,
            ImageView Cardbankimage,
            ViewGroup loader,
            View rotatingFrame

    ) {
        this.activity = activity;
        this.handler = handler;
        this.accountInfo = accountInfo;
        this.centerIcon = centerIcon;
        this.centerText = centerText;
        this.searching = searching;
        this.nextButton = nextButton;
        this.confirmRoot = confirmRoot;
        this.cardConfirm = cardConfirm;
        this.cardName = cardName;
        this.cardBank = cardBank;
        this.CardAccount = CardAccount;
        this.Cardbankimage = Cardbankimage;
        this.loader = loader;
        this.rotatingFrame = rotatingFrame;
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
            cardName.setText(accountInfo.getUserAccount());
            cardBank.setText(accountInfo.getUserBank());
            String number = accountInfo.getUserNumber();
            if (number != null && number.length() == 10) {
                String formatted = number.substring(0, 3) + " " +
                        number.substring(3, 6) + " " +
                        number.substring(6);
                CardAccount.setText(formatted);
            } else {
                CardAccount.setText(number);
            }
            Cardbankimage.setImageResource(accountInfo.getActivebank());
            confirmRoot.setVisibility(View.VISIBLE);
            cardConfirm.setOnClickListener(v1 -> {
                confirmRoot.setVisibility(View.GONE);
                LoaderHelper.startLoaderRotation(loader, rotatingFrame, ()->{
                    Intent intent = new Intent(activity, straighttodeposit.class);
                    activity.startActivity(intent);
                    activity.finish();
                });

            });

        });
    }
}

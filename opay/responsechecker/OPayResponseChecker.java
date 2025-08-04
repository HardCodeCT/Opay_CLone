package com.pay.opay.responsechecker;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.pay.opay.AccountInfo;
import com.pay.opay.R;
import com.pay.opay.adapter.AccountAdapter;
import com.pay.opay.animationhelper.AnimationUtilsHelper;
import com.pay.opay.database.Contact;

import java.util.ArrayList;
import java.util.List;

public class OPayResponseChecker {

    private final Handler handler;
    private final AccountInfo accountInfo;
    private final AccountAdapter accountAdapter;

    public OPayResponseChecker(Handler handler, AccountInfo accountInfo, AccountAdapter accountAdapter) {
        this.handler = handler;
        this.accountInfo = accountInfo;
        this.accountAdapter = accountAdapter;
    }

    public void check(View searching, View flag, View recyclerParent, View recycler, ImageView updateImage) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int status = accountInfo.getResponse();

                if (status == 1) {
                    AnimationUtilsHelper.stopRotating(updateImage);
                    searching.setVisibility(View.GONE);
                    flag.setVisibility(View.GONE);
                    recyclerParent.setVisibility(View.VISIBLE);
                    recycler.setVisibility(View.VISIBLE);

                    Contact contact = new Contact(
                            accountInfo.getUserAccount(),
                            accountInfo.getUserNumber(),
                            R.mipmap.profile_image
                    );

                    List<Contact> result = new ArrayList<>();
                    result.add(contact);

                    accountAdapter.updateList(result);
                    accountInfo.setBankUpdate(1);
                    accountInfo.setResponse(0);
                    accountInfo.setAccountType("Opay");

                } else if (status == 2) {
                    AnimationUtilsHelper.stopRotating(updateImage);
                    searching.setVisibility(View.GONE);
                    flag.setVisibility(View.VISIBLE);
                    accountInfo.setBankUpdate(1);
                    accountInfo.setResponse(0);
                } else {
                    // Retry check again in 1 second if status is not yet ready
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000); // Initial delay before first check
    }

}

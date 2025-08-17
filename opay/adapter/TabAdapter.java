package com.pay.opay.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pay.opay.fragments.FavouritesFragment;
import com.pay.opay.fragments.RecentsFragment;

public class TabAdapter extends FragmentStateAdapter {

    private final int loaderId;
    private final int progressBarId;
    private final int confirmRootId;
    private final int cardConfirmId;
    private final int cardNameId;
    private final int cardBankId;
    private final int cardAccountId;
    private final int cardBankImageId;

    public TabAdapter(
            @NonNull FragmentActivity fragmentActivity,
            int loaderId,
            int progressBarId,
            int confirmRootId,
            int cardConfirmId,
            int cardNameId,
            int cardBankId,
            int cardAccountId,
            int cardBankImageId
    ) {
        super(fragmentActivity);
        this.loaderId = loaderId;
        this.progressBarId = progressBarId;
        this.confirmRootId = confirmRootId;
        this.cardConfirmId = cardConfirmId;
        this.cardNameId = cardNameId;
        this.cardBankId = cardBankId;
        this.cardAccountId = cardAccountId;
        this.cardBankImageId = cardBankImageId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return RecentsFragment.newInstance(
                    loaderId,
                    progressBarId,
                    confirmRootId,
                    cardConfirmId,
                    cardNameId,
                    cardBankId,
                    cardAccountId,
                    cardBankImageId
            );
        } else { // position == 1
            return new FavouritesFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}


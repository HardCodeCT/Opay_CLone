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

    public TabAdapter(@NonNull FragmentActivity fragmentActivity, int loaderId, int progressBarId) {
        super(fragmentActivity);
        this.loaderId = loaderId;
        this.progressBarId = progressBarId;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return RecentsFragment.newInstance(loaderId, progressBarId);
            case 1:
                return new FavouritesFragment(); // if needed
            default:
                return RecentsFragment.newInstance(loaderId, progressBarId);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

package com.pay.opay.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pay.opay.fragments.FavouritesFragment;
import com.pay.opay.fragments.RecentsFragment;

public class TabAdapter extends FragmentStateAdapter {

    public TabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new RecentsFragment();
            case 1:
                return new FavouritesFragment();
            default:
                return new RecentsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

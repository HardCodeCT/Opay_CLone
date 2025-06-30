package com.pay.opay.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pay.opay.fragments.BankRecentFragment;
import com.pay.opay.fragments.FavouritesFragment;

public class BankTabAdapter extends FragmentStateAdapter {

    public BankTabAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new BankRecentFragment();
            case 1:
                return new FavouritesFragment();
            default:
                return new BankRecentFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
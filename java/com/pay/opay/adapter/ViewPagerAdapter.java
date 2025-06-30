package com.pay.opay.adapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pay.opay.fragments.CardsFragment;
import com.pay.opay.fragments.HomeFragment;
import com.pay.opay.fragments.RewardsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position) {
            case 0: return HomeFragment.newInstance("Home");
            case 1: return CardsFragment.newInstance("Search");
            case 2: return RewardsFragment.newInstance("Profile");
            default: throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
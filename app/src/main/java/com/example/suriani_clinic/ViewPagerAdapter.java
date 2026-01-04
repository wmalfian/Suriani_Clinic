package com.example.suriani_clinic;

import androidx.annotation.NonNull; // Fixed typo here
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Position 0 = First Tab (Home)
        // Position 1 = Second Tab (Manage)
        if (position == 1) {
            return new ManageFragment();
        }
        return new HomeFragment();
    }

    @Override
    public int getItemCount() {
        return 2; // We have 2 tabs
    }
}
package com.example.suriani_clinic;

import androidx.annotation.NonNull; // Fixed typo here
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

// This adapter is responsible for providing the fragments that will be displayed in the ViewPager2.
public class ViewPagerAdapter extends FragmentStateAdapter {

    // The constructor for the adapter.
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    // This method is called to create the fragment for a given position.
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Based on the position, a different fragment is returned.
        // Position 0 = First Tab (Home)
        // Position 1 = Second Tab (Manage)
        if (position == 1) {
            // If the position is 1, return a new instance of the ManageFragment.
            return new ManageFragment();
        }
        // For position 0 (and as a default), return a new instance of the HomeFragment.
        return new HomeFragment();
    }

    // This method returns the total number of items (fragments) in the adapter.
    @Override
    public int getItemCount() {
        return 2; // We have 2 tabs, so we return 2.
    }
}

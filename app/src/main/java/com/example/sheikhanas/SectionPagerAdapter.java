package com.example.sheikhanas;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionPagerAdapter extends FragmentPagerAdapter {

   private static final int NUM_TABS = 6;

    public SectionPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new EventsFragment();
            case 2:
                return new ScheduleFragment();
            case 3:
                return new ProjectFragment();
            case 4:
                return new NotificationFragment();
            case 5:
                return new MenuFragment();
                default:
                    return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }
}

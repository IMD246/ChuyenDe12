package com.example.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPageAdapter extends FragmentStatePagerAdapter {
    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:return new LearnManagementFragment();
            case 1:return new WordManagementFragment();
            case 2:return new AnalysicUser();
            case 3:return new AccountUserManagement();
            case 4:return new ProfileAdmin();
            default:return new LearnManagementFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}

package com.example.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPageAdapter extends FragmentStateAdapter {

    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:return new LearnManagementFragment();
            case 1:return new WordManagementFragment();
            case 2:return new AnalysicUser();
            case 3:return new AccountUserManagement();
//            case 4:return new ProfileAdmin();
            default:return new LearnManagementFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}


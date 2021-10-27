package com.example.menu_right.main_interface;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.menu_right.fragment.AlarmFragment;
import com.example.menu_right.fragment.VocabularyFragment;
import com.example.menu_right.learn.LearnFragment;
import com.example.menu_right.profile.ProfileFragment;
import com.example.menu_right.profile.SettingMenuFragment;

public class ViewPager2_Adapter extends FragmentStateAdapter {
    public ViewPager2_Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new LearnFragment();
            case 1:
                return new AlarmFragment();
            case 2:
                return new VocabularyFragment();
            case 3:
                return new ProfileFragment();
            case 4:
                return new SettingMenuFragment();
            default:
                return new LearnFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}

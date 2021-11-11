package com.example.EnglishBeginner.main_interface;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.EnglishBeginner.fragment.AlarmFragment;
import com.example.EnglishBeginner.fragment.LearnWord.vocubulary.VocabularyFragment;
import com.example.EnglishBeginner.learn.LearnFragment;
import com.example.EnglishBeginner.profile.ProfileFragment;
import com.example.EnglishBeginner.profile.SettingMenuFragment;

public class ViewPager2_Adapter extends FragmentStateAdapter {
    public ViewPager2_Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new AlarmFragment();
            case 2:
                return new VocabularyFragment();
            case 3:
                return new ProfileFragment();
            case 4:
                return new SettingMenuFragment();
            case 0:
            default:
                return new LearnFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}

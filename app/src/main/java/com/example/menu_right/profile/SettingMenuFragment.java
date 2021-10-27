package com.example.menu_right.profile;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.menu_right.R;
import com.example.menu_right.main_interface.MainActivity;

public class SettingMenuFragment extends Fragment {
    MainActivity activity;
    private Context context;
    public View myView;
    //khai báo
    Button btn_setting_profile, btn_setting_account, btn_setting_study_mode, btn_setting_privacy;

    public SettingMenuFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_setting_menu, container, false);
        setControl();
        setEvent();
        return myView;
    }

    private void setEvent() {
        btn_setting_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getContext(), EditProfileActivity.class);
                    getContext().startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(getContext(), "ko đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_setting_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(activity.getApplicationContext(), EditAccountActivity.class);
                    startActivity(intent);
                }catch (Exception e){
                    Log.d("ACBHD", e.getMessage());
                    Toast.makeText(getContext(), "ko đúng", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_setting_study_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getContext(), EditStudyModeActivity.class);
                    getContext().startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(getContext(), "ko đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_setting_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getContext(), EditPrivacyActivity.class);
                    getContext().startActivity(intent);
                }catch (Exception e) {
                    Toast.makeText(getContext(), "ko đúng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //ánh xạ các phần tử được khai báo
    private void setControl() {
        btn_setting_profile = myView.findViewById(R.id.btn_setting_editprofile);
        btn_setting_account = myView.findViewById(R.id.btn_setting_account);
        btn_setting_study_mode = myView.findViewById(R.id.btn_setting_study_mode);
        btn_setting_privacy = myView.findViewById(R.id.btn_setting_privacy);
    }
}

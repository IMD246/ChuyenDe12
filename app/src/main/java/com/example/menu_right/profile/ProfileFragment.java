package com.example.menu_right.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.menu_right.R;

public class ProfileFragment extends Fragment {
    private View myView;
    //khai báo button
    Button btnEditProfile;
    ImageButton btnEditAvatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_profile, container, false);
        setcontrol();
        setEvent();
        return myView;
    }

    private void setEvent() {
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chuyển hướng màn hình đến màn hình edit profile
                onClickGoToEditProfileScreen();
            }
        });
        btnEditAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //xử lí thay đổi avatar
                Toast.makeText(getContext(), "Xử lí thay đổi avatar!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setcontrol() {
        btnEditProfile = myView.findViewById(R.id.btn_profile_edit);
        btnEditAvatar = myView.findViewById(R.id.imgbtn_edit_avatar);
    }

    //hàm chuyển sang màn hình chỉnh sửa hồ sơ
    private void onClickGoToEditProfileScreen() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
//        Bundle bundle = new Bundle();
//        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }
}

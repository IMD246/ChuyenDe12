package com.example.EnglishBeginner.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.DTO.User;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingMenuFragment extends Fragment {
    public View myView;
    private ImageView imgUser;
    private TextView tvNameUser, tvProfile;
    private FirebaseUser user;
    private String userId;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    //khai báo
    private CardView btn_setting_profile, btn_setting_account, btn_setting_study_mode, btn_setting_privacy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_setting_menu, container, false);
        setControl();
        //Lấy dữ liệu của User từ Firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    if (!(user.getImageUser().trim().length() == 0 && user.getImageUser().isEmpty())) {
                        Glide.with(getContext()).load(user.getImageUser()).into(imgUser);
                    }
                    tvNameUser.setText(user.getFullname());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setEvent();
        return myView;
    }

    private void setEvent() {
        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserInterfaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("currentScreen", "3");
                intent.putExtras(bundle);
                getContext().startActivity(intent);
            }
        });
        btn_setting_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getContext(), EditProfileActivity.class);
                    getContext().startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(getContext(), "ko đúng:"+e, Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_setting_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getContext(), EditAccountActivity.class);
                    getContext().startActivity(intent);
                }catch (Exception e){
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
        tvProfile = myView.findViewById(R.id.gotoProfile);
        imgUser = myView.findViewById(R.id.img_user);
        tvNameUser = myView.findViewById(R.id.tv_name_user);
        btn_setting_profile = myView.findViewById(R.id.btn_setting_editprofile);
        btn_setting_account = myView.findViewById(R.id.btn_setting_account);
        btn_setting_study_mode = myView.findViewById(R.id.btn_setting_study_mode);
        btn_setting_privacy = myView.findViewById(R.id.btn_setting_privacy);
    }
}

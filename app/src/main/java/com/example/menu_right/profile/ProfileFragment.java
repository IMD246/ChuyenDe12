package com.example.menu_right.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.menu_right.DAO.DAOUserProfile;
import com.example.menu_right.DTO.User;
import com.example.menu_right.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    //khai báo
    private View myView;
    private DAOUserProfile daoUserProfile;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private User user;

    private Button btnEditProfile;
    private TextView tvFullname, tvAge, tvGender;
    private ImageView imgAvatar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_fragment_profile, container, false);
        setcontrol();
        setEvent();
        return myView;
    }

    //xử lí các sự kiện của View
    private void setEvent() {
        //Lấy dữ liệu của User từ Firebase
        getDataUserProfile();

        //gán dữ liệu của User vào các trường dữ liệu
        if(user == null){
            Toast.makeText(getContext(), "user null!", Toast.LENGTH_SHORT).show();
        }
        try {
            tvFullname.setText(daoUserProfile.getUser().getFullname());
//            tvFullname.setText(user.getFullname());
//            tvAge.setText(user.getAge());
//            tvGender.setText(user.getGender());

        } catch (Exception e) {
            Log.d("ERRO", ": " + e);
        }

        //Tạo sự kiện khi người dùng ấn vào nút "Sửa hồ sơ"
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chuyển hướng màn hình đến màn hình edit profile
                onClickGoToEditProfileScreen();
            }
        });
    }

    //Ánh xa, khởi gán,... cho các phần tử
    private void setcontrol() {
        //khởi tạo
        daoUserProfile = new DAOUserProfile(getContext());

        //Ánh xạ
        imgAvatar = myView.findViewById(R.id.img_avtar);
        tvFullname = myView.findViewById(R.id.tv_fullname);
        tvAge = myView.findViewById(R.id.tv_age);
        tvGender = myView.findViewById(R.id.tv_gender);
        btnEditProfile = myView.findViewById(R.id.btn_profile_edit);
    }


    private void getDataUserProfile() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            daoUserProfile.getDataFromRealTime(firebaseUser);
//            if (daoUserProfile.getUser() == null) {
//                Toast.makeText(getContext(), "user null", Toast.LENGTH_SHORT).show();
//            } else {
//                user = daoUserProfile.getUser();
//            }
            user = daoUserProfile.getUser();
        }
    }

    private void onClickGoToEditProfileScreen() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);

        Bundle bundle = new Bundle();

//        bundle.putString("user", firebaseUser.getUid());
//        bundle.putString("fullname", user.getFullname());
//        bundle.putString("age", String.valueOf(user.getAge()));

        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }
}

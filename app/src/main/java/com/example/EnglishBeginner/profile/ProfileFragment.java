package com.example.EnglishBeginner.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.EnglishBeginner.DAO.DAOUserProfile;
import com.example.EnglishBeginner.DTO.User;
import com.example.EnglishBeginner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {
    //khai báo
    private View myView;
    private DAOUserProfile daoUserProfile;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private Button btnEditProfile;
    private TextView tvFullname, tvAge, tvGender, tvTotalEXP, tvLevel;
    String userId;
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
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                getDataUserProfile(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        tvTotalEXP = myView.findViewById(R.id.tv_EXP);
    }

    //gán dữ liệu của User vào các trường dữ liệu
    private void getDataUserProfile(User user) {
        if (user != null) {
            tvFullname.setText(user.getFullname());
            tvAge.setText(String.valueOf(user.getAge()));
            tvGender.setText(user.getGender());
            if (!(user.getImageUser().trim().length() == 0 && user.getImageUser().isEmpty())) {
                Glide.with(getContext()).load(user.getImageUser()).into(imgAvatar);
            }
            tvTotalEXP.setText(String.valueOf(user.getTotalExp()));
        }
    }

    private void onClickGoToEditProfileScreen() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        getContext().startActivity(intent);
    }
}

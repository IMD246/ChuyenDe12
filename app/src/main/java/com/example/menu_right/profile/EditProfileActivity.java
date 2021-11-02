package com.example.menu_right.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.menu_right.DAO.DAOUserProfile;
import com.example.menu_right.DTO.User;
import com.example.menu_right.R;
import com.example.menu_right.main_interface.UserInterfaceActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{
    //kháo báo các phần tử
    private Button btnBack, btnSave;
    private ImageView imgUser;
    private EditText edtFullName,edtAge;
    private RadioButton rdbMale,rdbFemale,rdbOther;
    private FirebaseUser firebaseUser;
    private DAOUserProfile daoUserProfile;
    private User userProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setControl();
        getDataUserProfile();
        setEvent();
    }

    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditProfileActivity.this, UserInterfaceActivity.class));
            }
        });
    }
    //Ánh xạ
    private void setControl() {
        btnBack = findViewById(R.id.btn_edit_profile_back);
        btnBack.setOnClickListener(this);
        btnSave = findViewById(R.id.btn_edit_profile_save);
        btnSave.setOnClickListener(this);
        imgUser = findViewById(R.id.img_user);
        imgUser.setOnClickListener(this);
        edtFullName = findViewById(R.id.edtFullName);
        edtAge = findViewById(R.id.edtAge);
        rdbMale = findViewById(R.id.rdbMale);
        rdbFemale = findViewById(R.id.rdbFemale);
        rdbOther = findViewById(R.id.rdbOther);
        daoUserProfile = new DAOUserProfile(this);
    }
    private void getDataUserProfile() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        daoUserProfile.getDataFromRealTime(firebaseUser);
        if (daoUserProfile.getUser() != null)
        {
            userProfile = daoUserProfile.getUser();
            edtFullName.setText(userProfile.getFullname());
            edtAge.setText(String.valueOf(userProfile.getAge()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_edit_profile_save: updateProfile();
            case R.id.btn_edit_account_back: onBackPressed();
            case R.id.img_user: openFileChoose();
        }
    }

    private void openFileChoose() {
    }

    private void updateProfile() {
        String fullName = edtFullName.getText().toString().trim();
        int age = Integer.parseInt(edtAge.getText().toString().trim());
        String gender = "Male";
        if (fullName.isEmpty() || fullName.length() == 0) {
            edtFullName.setError("Không được để trống");
            edtFullName.requestFocus();
            return;
        }
        else if (age>12 && age<100)
        {
            edtAge.setError("Đổ tuổi phải trong khoảng 12 tới 99");
            edtAge.requestFocus();
            return;
        }
        else if (String.valueOf(age).trim().length() == 0)
        {
            edtAge.setError("Không được để trống");
            edtAge.requestFocus();
            return;
        }
        else {
            userProfile.setFullname(edtFullName.getText().toString());
            userProfile.setAge(Integer.parseInt(edtAge.getText().toString()));
            userProfile.setGender(gender);
            HashMap<String,Object>hashMap = new HashMap<>();
            hashMap.put("fullname",userProfile.getFullname());
            hashMap.put("age",userProfile.getAge());
            hashMap.put("gender",userProfile.getGender());
            daoUserProfile.updateProFileUser(hashMap, firebaseUser);
        }
    }
}
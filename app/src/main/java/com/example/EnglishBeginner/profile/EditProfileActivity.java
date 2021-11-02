package com.example.EnglishBeginner.profile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.example.EnglishBeginner.DAO.DAOImageStorage;
import com.example.EnglishBeginner.DAO.DAOUserProfile;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    //kháo báo các phần tử
    private Button btnBack, btnSave;
    private ImageView imgUser;
    private EditText edtFullName, edtAge;
    private RadioButton rdbMale, rdbFemale, rdbOther;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private DAOUserProfile daoUserProfile;
    private String userId;
    private String gender = DEFAULTVALUE.OTHER;
    private DAOImageStorage daoImageStorage;
    private User userOld = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setControl();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userOld.setAge(user.getAge());
                userOld.setFullname(user.getFullname());
                userOld.setGender(user.getGender());
                userOld.setImageUser(user.getImageUser());
                getDataUserProfileToControl(user);
                gender = user.getGender();
                setRadioButtonForGender(user.getGender());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setEvent();
    }

    private void getDataUserProfileToControl(User user) {
        if (user != null) {
            edtFullName.setText(user.getFullname());
            edtAge.setText(String.valueOf(user.getAge()));
            if (!(user.getImageUser().trim().length() == 0 && user.getImageUser().isEmpty())) {
                Picasso.get().load(user.getImageUser()).resize(100, 100).into(imgUser);
            }
        }
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
        daoImageStorage = new DAOImageStorage(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_profile_save:
                updateProfile();
                break;
            case R.id.btn_edit_account_back:
                onBackPressed();
                break;
            case R.id.img_user:
                openFileChoose();
                break;
        }
    }

    private void openFileChoose() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            daoImageStorage.setmImgURL(data.getData());
            imgUser.setImageURI(daoImageStorage.getmImgURL());
        }
    }

    private void updateProfile() {
        Boolean[] booleans = new Boolean[3];
        for (int i = 0; i < booleans.length; i++) {
            booleans[i] = true;
        }
        String fullName = edtFullName.getText().toString().trim();
        int age = 0;
        if (edtAge.getText().toString().trim().length() > 0) {
            age = Integer.parseInt(edtAge.getText().toString());
        }
        if (fullName.isEmpty() || fullName.length() == 0) {
            booleans[0] = false;
            edtFullName.setError("Không được để trống");
            edtFullName.requestFocus();
        } else if (age == 0) {
            booleans[2] = false;
            edtAge.setError("Không được để trống");
            edtAge.requestFocus();
        } else if (age < 12 || age > 100) {
            booleans[1] = false;
            edtAge.setError("Đổ tuổi phải trong khoảng 12 tới 99");
            edtAge.requestFocus();
        } else {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("fullname", fullName);
            hashMap.put("age", age);
            hashMap.put("gender", getGender());
            if (hashMap.get("fullname").toString().equalsIgnoreCase(userOld.getFullname())) {
                daoUserProfile.updateProFileUser(hashMap, firebaseUser.getUid());
            }
            daoImageStorage.uploadFileImageUser("User", firebaseUser.getUid(), userOld);

        }
    }

    private void setRadioButtonForGender(String gender1) {
        if (gender1.equalsIgnoreCase(DEFAULTVALUE.MALE)) {
            rdbMale.setChecked(true);
        } else if (gender1.equalsIgnoreCase(DEFAULTVALUE.FEMALE)) {
            rdbFemale.setChecked(true);
        } else if(gender1.equalsIgnoreCase(DEFAULTVALUE.OTHER)) {
            rdbOther.setChecked(true);
        }
    }

    private String getGender() {
        if (rdbMale.isChecked()){
            gender = DEFAULTVALUE.MALE;
        }else if (rdbFemale.isChecked()){
            gender = DEFAULTVALUE.FEMALE;
        }else{
            gender = DEFAULTVALUE.OTHER;
        }
        return gender;
    }
}
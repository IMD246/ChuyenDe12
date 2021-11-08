package com.example.EnglishBeginner.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.EnglishBeginner.DAO.DAOUserProfile;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.User;
import com.example.EnglishBeginner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class EditPrivacyActivity extends AppCompatActivity implements View.OnClickListener {
    //khai báo
    private CheckBox cbPrivacy;
    private Button btnBack;

    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private String userId;
    private DAOUserProfile daoUserProfile;
    private User userOld = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_privacy);


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userOld.setActivePrivacy(user.isActivePrivacy());
                getDataUserProfileToControl(user);
                setCheckBoxPrivacy(user.isActivePrivacy());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        cbPrivacy = findViewById(R.id.cb_privacy_public_profile);
        daoUserProfile = new DAOUserProfile(this);

        cbPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Boolean aBoolean = userOld.isActivePrivacy();
                if (isChecked){
                    aBoolean = true;
                }else{
                    aBoolean = false;
                }
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("activePrivacy", aBoolean);
                daoUserProfile.updateProFileUser(hashMap, firebaseUser.getUid());
            }
        });
        btnBack = findViewById(R.id.btn_back_pr);
        btnBack.setOnClickListener(this);
    }

    private void getDataUserProfileToControl(User user) {
        if (user != null) {
            if (user.isActivePrivacy()) {
                cbPrivacy.setChecked(true);
            } else {
                cbPrivacy.setChecked(false);
            }
        }
    }

    private void setCheckBoxPrivacy(Boolean privacy) {
        if (privacy) {
            cbPrivacy.setChecked(true);
        } else {
            cbPrivacy.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back_pr:
                EditPrivacyActivity.this.finish();
                break;
        }
    }
}
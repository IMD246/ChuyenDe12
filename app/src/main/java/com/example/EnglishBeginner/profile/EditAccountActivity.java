package com.example.EnglishBeginner.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.EnglishBeginner.DAO.DAOUserProfile;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.HashPass;
import com.example.EnglishBeginner.DTO.User;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class EditAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnBack, btnSave;
    private EditText ipCurrentPass, ipNewPass, ipVerify;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private DAOUserProfile daoUserProfile;
    private String userId;
    private String gender = DEFAULTVALUE.OTHER;
    private User userOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);
        setControl();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userOld = user;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setControl() {
        btnBack = findViewById(R.id.btn_edit_account_back);
        daoUserProfile = new DAOUserProfile(this);
        btnBack.setOnClickListener(this);
        btnSave = findViewById(R.id.btn_edit_account_save);
        btnSave.setOnClickListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        ipCurrentPass = findViewById(R.id.edtCurrentPass);
        ipNewPass = findViewById(R.id.ip_new_pass);
        ipVerify = findViewById(R.id.ip_verify);
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit_account_back:
//                Intent intent = new Intent(EditAccountActivity.this, UserInterfaceActivity.class);
//                EditAccountActivity.this.startActivity(intent);
                EditAccountActivity.this.finish();
                break;
            case R.id.btn_edit_account_save:
                try {
                    changePassWord();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    // h??m n??y d??ng ????? ?????i m???t kh???u
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void changePassWord() throws Exception {
        String current = ipCurrentPass.getText().toString();
        String newPass = ipNewPass.getText().toString();
        String verify = ipVerify.getText().toString();
        if (current.isEmpty()) {
            ipCurrentPass.setError("Kh??ng b??? tr???ng");
            ipCurrentPass.requestFocus();
        }
        else if (newPass.isEmpty()) {
            ipNewPass.setError("Kh??ng b??? tr???ng");
            ipNewPass.requestFocus();
        }
        else if (newPass.length() < 6) {
            ipNewPass.setError("????? d??i nh??? nh???t c???a m???t kh???u l?? 6");
            ipNewPass.requestFocus();
        }
        else if (verify.isEmpty()) {
            ipVerify.setError("Kh??ng b??? tr???ng");
            ipVerify.requestFocus();
        }
        else if (!verify.equalsIgnoreCase(ipNewPass.getText().toString()))
        {
            ipVerify.setError("M???t kh???u kh??ng tr??ng nhau");
            ipVerify.requestFocus();
        }
        else
        {
            String decryptCurrentPass = HashPass.decryptPass(userOld.getPassWord(),firebaseUser.getUid());
            Log.d("test", decryptCurrentPass);
            if (!(decryptCurrentPass.equalsIgnoreCase(current))) {
                ipCurrentPass.setError("M????t kh????u hi????n ta??i kh??ng chi??nh xa??c");
                ipCurrentPass.requestFocus();
            }
            else {
                user.updatePassword(newPass).addOnSuccessListener(unused -> {
                    Toast.makeText(EditAccountActivity.this, "?????i m???t kh???u th??nh c??ng", Toast.LENGTH_SHORT).show();
                    HashMap<String,Object>hashMap = new HashMap<>();
                    try {
                        String encryptPass = HashPass.encryptPass(user.getUid(),newPass);
                        hashMap.put("passWord",encryptPass);
                        daoUserProfile.updatePassWordUser(hashMap,user.getUid());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditAccountActivity.this, "?????i m???t kh???u kh??ng th??nh c??ng", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


}
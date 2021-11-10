package com.example.EnglishBeginner.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.EnglishBeginner.DAO.DAOImageStorage;
import com.example.EnglishBeginner.DAO.DAOUserProfile;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.User;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class EditAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnBack, btnSave;
    private EditText ipEmail, ipNewPass, ipVerify;
    private FirebaseUser user;

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
        setContentView(R.layout.activity_edit_account);
        setControl();
    }

    private void setControl() {
        btnBack = findViewById(R.id.btn_edit_account_back);
        btnBack.setOnClickListener(this);
        btnSave = findViewById(R.id.btn_edit_account_save);
        btnSave.setOnClickListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        ipEmail = findViewById(R.id.ip_email);
        ipNewPass = findViewById(R.id.ip_new_pass);
        ipVerify = findViewById(R.id.ip_verify);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit_account_back:
                Intent intent = new Intent(EditAccountActivity.this, UserInterfaceActivity.class);
                EditAccountActivity.this.startActivity(intent);
                break;
            case R.id.btn_edit_account_save:
                changePassWord();
                break;
        }
    }

    // hàm này dùng để đổi mật khẩu
    private void changePassWord() {
        String Email = ipEmail.getText().toString();
        String newPass = ipNewPass.getText().toString();
        String verify = ipVerify.getText().toString();
        if (Email.isEmpty()) {
            ipEmail.setError("Không bỏ trống");
            ipEmail.requestFocus();
        }else if (!user.getEmail().equalsIgnoreCase(Email)){
            ipEmail.setError("địa chỉ email không chính xác");
            ipEmail.requestFocus();
        } else if (newPass.isEmpty()) {
            ipNewPass.setError("Không bỏ trống");
            ipNewPass.requestFocus();
        }
        else if (newPass.length() < 6) {
            ipNewPass.setError("Độ dài nhỏ nhất của mật khẩu là 6");
            ipNewPass.requestFocus();
        }
        else if (verify.isEmpty()) {
            ipVerify.setError("Không bỏ trống");
            ipVerify.requestFocus();
        }
        else if (!verify.equalsIgnoreCase(ipNewPass.getText().toString()))
        {
            ipVerify.setError("Mật khẩu không trùng nhau");
            ipVerify.requestFocus();
        }
        else
        {
            user.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(EditAccountActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditAccountActivity.this, "Đổi mật khẩu không thành công", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


}
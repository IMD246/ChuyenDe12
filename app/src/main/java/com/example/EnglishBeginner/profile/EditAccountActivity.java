package com.example.EnglishBeginner.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class EditAccountActivity extends AppCompatActivity {
    private Button btnBack, btnSave;
    private EditText ipCurrentPass, ipNewPass, ipVerify;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_account);

        setControl();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditAccountActivity.this, UserInterfaceActivity.class);
                EditAccountActivity.this.startActivity(intent);
            }
        });
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String currentPass = ipCurrentPass.getText().toString();
//
//                changePassWord();
//            }
//        });
    }

    private void setControl() {
        btnBack = findViewById(R.id.btn_edit_account_back);
        btnSave = findViewById(R.id.btn_edit_account_save);
////        user = FirebaseAuth.getInstance().getCurrentUser();
        ipCurrentPass = findViewById(R.id.ip_curent_pass);
        ipNewPass = findViewById(R.id.ip_new_pass);
        ipVerify = findViewById(R.id.ip_verify);
    }
    // hàm này dùng để đổi mật khẩu
    private void changePassWord() {
        String newPass = ipNewPass.getText().toString();
        String verifyPass = ipVerify.getText().toString();
        if (newPass.isEmpty()) {
            ipNewPass.setError("Không bỏ trống");
            ipNewPass.requestFocus();
        }
        else if (newPass.length() < 6) {
            ipNewPass.setError("Độ dài nhỏ nhất của mật khẩu là 6");
            ipNewPass.requestFocus();
        }
        else if (verifyPass.isEmpty()) {
            ipVerify.setError("Không bỏ trống");
            ipVerify.requestFocus();
        }
        else if (!verifyPass.equalsIgnoreCase(newPass))
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
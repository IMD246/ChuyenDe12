package com.example.englishbeginner.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.englishbeginner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity_Admin extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNewPass,edtVerifyPass;
    private Button btnYes, btnBack;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initUI();
    }
    // ánh xạ view và lấy dữ liệu authenticate khi user đăng nhập
    private void initUI() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        btnBack = findViewById(R.id.btn_edit_account_back);
        btnBack.setOnClickListener(this);
        edtNewPass = findViewById(R.id.edtNewPass);
        edtVerifyPass = findViewById(R.id.edtVerifyNewPass);
        btnYes = findViewById(R.id.btn_edit_account_save);
        btnYes.setOnClickListener(this);
    }
    // set onClick dựa vào id của view
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_edit_account_save : changePassWord();
            break;
            case R.id.btn_edit_account_back:
                Intent intent = new Intent(ChangePasswordActivity_Admin.this, AdminInterface.class);
                startActivity(intent);
                break;
        }
    }
    // hàm này dùng để đổi mật khẩu
    private void changePassWord() {
        String newPass = edtNewPass.getText().toString();
        String verifyPass = edtVerifyPass.getText().toString();
        if (newPass.isEmpty()) {
            edtNewPass.setError("Không bỏ trống");
            edtNewPass.requestFocus();
        }
        else if (newPass.length() < 6) {
            edtNewPass.setError("Độ dài nhỏ nhất của mật khẩu là 6");
            edtNewPass.requestFocus();
        }
        else if (verifyPass.isEmpty()) {
            edtVerifyPass.setError("Không bỏ trống");
            edtVerifyPass.requestFocus();
        }
        else if (!verifyPass.equalsIgnoreCase(newPass))
        {
            edtVerifyPass.setError("Mật khẩu không trùng nhau");
            edtVerifyPass.requestFocus();
        }
        else
        {
            user.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                        Toast.makeText(ChangePasswordActivity_Admin.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ChangePasswordActivity_Admin.this, "Đổi mật khẩu không thành công", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
package com.example.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity_Admin extends AppCompatActivity implements View.OnClickListener {

    private EditText edtNewPass,edtVerifyPass;
    private Button btnYes;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        initUI();
    }

    private void initUI() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        edtNewPass = findViewById(R.id.edtNewPass);
        edtVerifyPass = findViewById(R.id.edtVerifyNewPass);
        btnYes = findViewById(R.id.btn_edit_account_save);
        btnYes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_edit_account_save : changePassWord();
            break;
        }
    }
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
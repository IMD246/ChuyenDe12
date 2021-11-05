package com.example.EnglishBeginner.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.EnglishBeginner.Login.Login;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.User;
import com.example.EnglishBeginner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private TextView registeruser;
    private EditText edtextEmail, edtextPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        mAuth = FirebaseAuth.getInstance();
        registeruser = (Button) findViewById(R.id.btnRegister);
        registeruser.setOnClickListener(this);

        edtextEmail = (EditText) findViewById(R.id.edtEmail);
        edtextPassword = (EditText) findViewById(R.id.edtPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                registerUser();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterUser.this, Login.class));
    }

    private void registerUser() {
        firestore = FirebaseFirestore.getInstance();
        Boolean check = true;
        String email = edtextEmail.getText().toString();
        String password = edtextPassword.getText().toString();
        if (email.isEmpty()) {
            edtextEmail.setError("Email là bắt buộc");
            edtextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtextEmail.setError("Làm ơn cung cấp một email hợp lệ!");
            edtextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            edtextPassword.setError("Mật khẩu là bắt buộc");
            edtextPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            edtextPassword.setError("Độ dài nhỏ nhất của mật khẩu là 6");
            edtextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException existEmail) {
                        Toast.makeText(RegisterUser.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    } catch (Exception e) {
                        Toast.makeText(RegisterUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else {
                    User us = new User("",email,"other",0, 0, 0, 0, "", true);
                    FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(us)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        Toast.makeText(RegisterUser.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                        DocumentReference df = firestore.collection("users").document(user.getUid());
                                        HashMap<String,Object> auThenticateUser = new HashMap<>();
                                        auThenticateUser.put("email",us.getEmail());
                                        auThenticateUser.put("authenticate", DEFAULTVALUE.USER);
                                        auThenticateUser.put("isBlock",false);
                                        auThenticateUser.put("isOnline",false);
                                        df.set(auThenticateUser);
                                        progressBar.setVisibility(View.VISIBLE);
                                        startActivity(new Intent(RegisterUser.this, Login.class));
                                    } else {
                                        Toast.makeText(RegisterUser.this, "Đăng ký thất bại , Hãy thử lại", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                }
            }
        });
    }
}
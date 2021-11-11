package com.example.EnglishBeginner.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.EnglishBeginner.Admin.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.Admin.DTO.User;
import com.example.EnglishBeginner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
        mAuth = FirebaseAuth.getInstance();
        TextView registerUser = findViewById(R.id.btnRegister);
        registerUser.setOnClickListener(this);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        progressBar = findViewById(R.id.progressBar);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnRegister) {
            registerUser();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterUser.this, Login.class));
    }

    private void registerUser() {
        firestore = FirebaseFirestore.getInstance();
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        if (email.isEmpty()) {
            edtEmail.setError("Email là bắt buộc");
            edtEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Làm ơn cung cấp một email hợp lệ!");
            edtEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            edtPassword.setError("Mật khẩu là bắt buộc");
            edtPassword.requestFocus();
            return;
        }
        if (password.length() < 6) {
            edtPassword.setError("Độ dài nhỏ nhất của mật khẩu là 6");
            edtPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthUserCollisionException existEmail) {
                    Toast.makeText(RegisterUser.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } catch (Exception e) {
                    Toast.makeText(RegisterUser.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            } else {
                User us = new User("", email, 0, 0, 0, -1, "", true);
                FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(us)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                Toast.makeText(RegisterUser.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                DocumentReference df = firestore.collection("users").document(user.getUid());
                                HashMap<String,Object> authenticateUser = new HashMap<>();
                                authenticateUser.put("email",us.getEmail());
                                authenticateUser.put("authenticate", DEFAULTVALUE.ADMIN);
                                authenticateUser.put("isBlock", false);
                                df.set(authenticateUser);
                                progressBar.setVisibility(View.VISIBLE);
                                startActivity(new Intent(RegisterUser.this, Login.class));
                            } else {
                                Toast.makeText(RegisterUser.this, "Đăng ký thất bại , Hãy thử lại", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        });
    }
}
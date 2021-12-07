package com.example.EnglishBeginner.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.EnglishBeginner.DAO.DAOTopic;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.HashPass;
import com.example.EnglishBeginner.DTO.ProcessTopicItem;
import com.example.EnglishBeginner.DTO.User;
import com.example.EnglishBeginner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private DAOTopic daoTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);
        daoTopic = new DAOTopic(this);
        daoTopic.getDataFromRealTimeFirebase();
        mAuth = FirebaseAuth.getInstance();
        TextView registerUser = (Button) findViewById(R.id.btnRegister);
        registerUser.setOnClickListener(this);

        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @SuppressLint("NewApi")
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

    @RequiresApi(api = Build.VERSION_CODES.O)
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
                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                User us = new User("",email,"other",0, 0, 0, 0, "", true);
                try {
                    us.setPassWord(HashPass.encryptPass(firebaseUser.getUid(),edtPassword.getText().toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                FirebaseDatabase.getInstance().getReference("users").child(Objects.requireNonNull(firebaseUser.getUid())).setValue(us)
                        .addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                Toast.makeText(RegisterUser.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("listProcessUser/"+user.getUid()+"/listtopic");
                                for (int i=0;i<daoTopic.getTopicList().size();i++)
                                {
                                    for (int j=1;j<=2;j++) {
                                        ProcessTopicItem processTopicItem = new ProcessTopicItem(j,0,daoTopic.getTopicList().get(i).getId());
                                        databaseReference.child(daoTopic.getTopicList().get(i).getId()+"/listProcess/"+processTopicItem.getProcess()).setValue(processTopicItem).isComplete();
                                    }
                                }
                                DocumentReference df = firestore.collection("users").document(user.getUid());
                                HashMap<String,Object> authenticateUser = new HashMap<>();
                                authenticateUser.put("email",us.getEmail());
                                authenticateUser.put("authenticate", DEFAULTVALUE.USER);
                                authenticateUser.put("isBlock",false);
                                authenticateUser.put("isOnline",false);
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
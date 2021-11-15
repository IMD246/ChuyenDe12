package com.example.EnglishBeginner.Login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.EnglishBeginner.DAO.DAOUserProfile;
import com.example.EnglishBeginner.DTO.DEFAULTVALUE;
import com.example.EnglishBeginner.DTO.HashPass;
import com.example.EnglishBeginner.DTO.User;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.main_interface.UserInterfaceActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassWord;
    private CallbackManager callbackManager;
    private FirebaseAuth mFirebaseAuth;
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private static final String TAO = "FacebookAuthentication";
    private static final String EMAIL = "email";
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 100;
    private FirebaseUser current;
    private int dem = 0;
    private User userProfile;
    private DAOUserProfile daoUserProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.login);
        createRequestGoogle();
        mFirebaseAuth = FirebaseAuth.getInstance();
        setControl();
    }

    private void createRequestGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void loginFacebookRegister() {
        LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList(EMAIL, "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAO, "OnCancel");
            }

            @Override
            public void onError(@NonNull FacebookException error) {
                Log.d(TAO, "OnError" + error);
            }
        });
    }

    // Ánh xạ tới các view trong layout
    private void setControl() {
        daoUserProfile = new DAOUserProfile(this);
        Button loginGoogle = findViewById(R.id.loginGoogle);
        TextView register = findViewById(R.id.tvRegister);
        register.setOnClickListener(this);
        TextView tvForgetPassWord = findViewById(R.id.tvForgotPass);
        tvForgetPassWord.setOnClickListener(this);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassWord = findViewById(R.id.edtPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
        Button loginFacebook;
        loginGoogle.setOnClickListener(v -> signIn());
        loginFacebook = findViewById(R.id.loginFB);
        loginFacebook.setOnClickListener(this);
    }

    // Dùng hàm xử lý nút quay lại của thiết bị
    @Override
    public void onBackPressed() {
        alertDialog();
    }

    // Xây dựng một Hộp thoại thông báo
    public void alertDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Bạn có muốn thoát khỏi ứng dụng không?");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Có",
                (dialog, id) -> {
                    finish();
                    System.exit(0);
                });

        builder1.setNegativeButton(
                "Không",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void linkWithInWithFirebase(AuthCredential credential) {
        Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).linkWithCredential(credential).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                try {
                    current = Tasks.await(mFirebaseAuth.signInWithCredential(credential)).getUser();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
//        linkWithInWithFirebase(credential);
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                assert firebaseUser != null;
                checkAuthenticate(firebaseUser.getUid(), dem);
                Log.d("signIn", "GoogleLoginSuccessful");
            }
        });
    }

    private void handleFacebookToken(@NonNull AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
//        linkWithInWithFirebase(credential);
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                assert firebaseUser != null;
                checkAuthenticate(firebaseUser.getUid(), dem);
                Log.d("signIn", "FacebookLoginSuccessful");
            }
        });
    }

    @SuppressLint({"NonConstantResourceId", "NewApi"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRegister:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.btnLogin:
                try {
                    Handlelogin();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.loginFB:
                loginFacebookRegister();
                break;
            case R.id.tvForgotPass:
                startActivity(new Intent(this, ForgetPassword.class));
        }
    }

    private void checkAuthenticate(String uid, int dem) {
        if (uid != null) {
            DocumentReference documentReference = firestore.collection("users").document(uid);
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                if (Objects.equals(documentSnapshot.getBoolean("isBlock"), false)) {
                    if (Objects.requireNonNull(documentSnapshot.getString("authenticate")).equalsIgnoreCase(DEFAULTVALUE.USER)) {
                        startActivity(new Intent(Login.this, UserInterfaceActivity.class));
                    } else {
                        DEFAULTVALUE.alertDialogMessage("Thông báo", "Không thuộc phạm vi người dùng", Login.this);
                    }
                } else if (Objects.equals(documentSnapshot.getBoolean("isBlock"), true)) {
                    final String msg = "Tài khoản " + documentSnapshot.getString("email") + " hiện tại đã bị khóa";
                    DEFAULTVALUE.alertDialogMessage("Thông báo", msg, Login.this);
                } else {
                    if (dem > 0) {
                        DEFAULTVALUE.alertDialogMessage("Thông báo", "Hãy đăng ký tài khoản", Login.this);
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void Handlelogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassWord.getText().toString().trim();
        if (email.isEmpty()) {
            edtEmail.setError("Email is required");
            edtEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Please provide valid email!");
            edtEmail.requestFocus();
        } else if (password.isEmpty()) {
            edtPassWord.setError("Password is required");
            edtPassWord.requestFocus();
        } else if (password.length() < 6) {
            edtPassWord.setError("Min password length should be 6 characters");
            edtPassWord.requestFocus();
        } else {
            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    assert user != null;
                    if (user.isEmailVerified()) {
                        try {
                            String encryptPass = HashPass.encryptPass(user.getUid(), password);
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("passWord", encryptPass);
                            daoUserProfile.updatePassWordUser(hashMap, user.getUid());
                            dem++;
                            checkAuthenticate(user.getUid(), dem);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        user.sendEmailVerification();
                        DEFAULTVALUE.alertDialogMessage("Thông báo", "Hãy xác thực email của bạn!", Login.this);
                    }
                } else {
                    String errorCode = ((FirebaseAuthException) Objects.requireNonNull(task.getException())).getErrorCode();
                    switch (errorCode) {
                        case "ERROR_INVALID_EMAIL":
                            edtEmail.setError("Định dạng email không phù hợp");
                            edtEmail.requestFocus();
                            break;
                        case "ERROR_WRONG_PASSWORD":
                            edtPassWord.setError("Sai Mật Khẩu");
                            edtPassWord.requestFocus();
                            break;
                        case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                            Toast.makeText(Login.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_EMAIL_ALREADY_IN_USE":
                            edtEmail.setError("Địa chỉ email này đã có người sử dụng");
                            edtEmail.requestFocus();
                            break;

                        case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                            Toast.makeText(Login.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_NOT_FOUND":
                            DEFAULTVALUE.alertDialogMessage("Thông báo", "Tài khoản không tồn tại , hãy đăng ký tài khoản", Login.this);
                            break;
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        current = firebaseAuth.getCurrentUser();
        if (current != null) {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users/" + current.getUid());
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userProfile = snapshot.getValue(User.class);
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            assert userProfile != null;
                            firebaseAuth.signInWithEmailAndPassword(userProfile.getEmail(),
                                    HashPass.decryptPass(userProfile.getPassWord(), current.getUid())).addOnCompleteListener(task -> {
                                if (current.isEmailVerified()) {
                                    dem++;
                                    checkAuthenticate(current.getUid(), dem);
                                } else {
                                    current.sendEmailVerification();
                                    DEFAULTVALUE.alertDialogMessage("Thông báo", "Hãy xác thực email của bạn!", Login.this);
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }
}
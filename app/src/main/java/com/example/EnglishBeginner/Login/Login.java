package com.example.EnglishBeginner.Login;

import static com.facebook.FacebookSdk.sdkInitialize;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.EnglishBeginner.Admin.AdminInterface;
import com.example.EnglishBeginner.DEFAULTVALUE;
import com.example.EnglishBeginner.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.Utils;

import java.util.Arrays;
import java.util.Objects;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassWord;
    private CallbackManager callbackManager;
    private FirebaseAuth mFirebaseAuth;
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private LoginButton loginFacebook;
    private static final String TAO = "FacebookAuthentication";
    private static final String EMAIL = "email";
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 100;
    private int dem=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());
        setContentView(R.layout.login);
        createRequestGoogle();
        mFirebaseAuth = FirebaseAuth.getInstance();
        setControl();
        loginFacebookRegister();
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
        loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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
        SignInButton loginGoogle = findViewById(R.id.loginGoogle);
        loginGoogle.setSize(SignInButton.SIZE_STANDARD);
        TextView register = findViewById(R.id.tvRegister);
        register.setOnClickListener(this);
        TextView tvForgetPassWord = findViewById(R.id.tvForgotPass);
        tvForgetPassWord.setOnClickListener(this);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassWord = findViewById(R.id.edtPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        callbackManager = CallbackManager.Factory.create();
        loginFacebook = findViewById(R.id.loginFB);
        loginFacebook.setPermissions(Arrays.asList(EMAIL, "public_profile"));
        loginGoogle.setOnClickListener(v -> signIn());
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
        }
        else
        {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void linkWithInWithFirebase(AuthCredential credential) {
        mFirebaseAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                    FirebaseUser prevUser = currentUser;
                    try {
                        currentUser = Tasks.await(mFirebaseAuth.signInWithCredential(credential)).getUser();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        dem++;
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        linkWithInWithFirebase(credential);
    }

    private void handleFacebookToken(@NonNull AccessToken accessToken) {
        dem++;
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        linkWithInWithFirebase(credential);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRegister:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.btnLogin:
                Handlelogin();
                break;
            case R.id.tvForgotPass:
                startActivity(new Intent(this, ForgetPassword.class));
        }
    }

    private void checkAuthenticate(String uid,int dem) {
        if (uid!=null) {
            DocumentReference documentReference = firestore.collection("users").document(uid);
            documentReference.get().addOnSuccessListener(documentSnapshot -> {
                if (Objects.equals(documentSnapshot.getBoolean("isBlock"), false)) {
                    if (Objects.requireNonNull(documentSnapshot.getString("authenticate")).equalsIgnoreCase(DEFAULTVALUE.ADMIN)) {
                        startActivity(new Intent(Login.this, AdminInterface.class));
                    } else if (Objects.requireNonNull(documentSnapshot.getString("authenticate")).equalsIgnoreCase(DEFAULTVALUE.USER)) {
                        DEFAULTVALUE.alertDialogMessage("Thông báo", "Không thuộc quyền admin", Login.this);
                    }
                } else if (Objects.equals(documentSnapshot.getBoolean("isBlock"), true)) {
                    final String msg = "Tài khoản " + documentSnapshot.getString("email") + " hiện tại đã bị khóa";
                    DEFAULTVALUE.alertDialogMessage("Thông báo", msg, Login.this);
                } else {
                    if (dem>0) {
                        DEFAULTVALUE.alertDialogMessage("Thông báo", "Hãy đăng ký tài khoản", Login.this);
                    }
                }
            });
        }
    }

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
        }
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = mFirebaseAuth.getCurrentUser();
                assert user != null;
                if (user.isEmailVerified()) {
                    checkAuthenticate(user.getUid(),dem);
                    dem++;
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
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            checkAuthenticate(firebaseUser.getUid(),dem);
            dem++;
        }
    }
}
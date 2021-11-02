package com.example.EnglishBeginner.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.EnglishBeginner.Admin.AdminInterface;
import com.example.EnglishBeginner.DEFAULTVALUE;
import com.example.EnglishBeginner.R;
import com.example.EnglishBeginner.User.UserInterface;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView register, tvForgetPassWord;
    private EditText edtEmail, edtPassWord;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private AccessTokenTracker accessTokenTracker;
    private ImageView imgProfile;
    private Button btnLogin;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private LoginButton loginFacebook;
    private SignInButton loginGoogle;
    private static final String TAO = "FacebookAuthentication";
    private static final String EMAIL = "email";
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private final static int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.login);
        setControl();
//        loginFacebookRegister();
//        getDataAndTrackerToken();
//        createRequestGoogle();
    }

    private void createRequestGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void getDataAndTrackerToken() {
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    updateUI(user);
                } else {
                    updateUI(null);
                }
            }
        };
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    mFirebaseAuth.signOut();
                }
            }
        };
    }

    private void loginFacebookRegister() {
        loginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAO, "onSuccess" + loginResult);
                handleFacebookToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAO, "OnCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAO, "OnError" + error);
            }
        });
    }

    // Ánh xạ tới các view trong layout
    private void setControl() {
        register = (TextView) findViewById(R.id.tvRegister);
        register.setOnClickListener(this);
        tvForgetPassWord = findViewById(R.id.tvForgotPass);
        tvForgetPassWord.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        edtEmail = findViewById(R.id.edtEmail);
        edtPassWord = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        loginGoogle = findViewById(R.id.loginGoogle);
        loginGoogle.setSize(SignInButton.SIZE_STANDARD);
        loginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        callbackManager = CallbackManager.Factory.create();
        loginFacebook = (LoginButton) findViewById(R.id.loginFB);
        loginFacebook.setPermissions(Arrays.asList(EMAIL, "public_profile"));

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
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        System.exit(0);
                    }
                });

        builder1.setNegativeButton(
                "Không",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }
                    }
                });
    }

    private void handleFacebookToken(AccessToken accessToken) {
        Log.d(TAO, "handleFacebookToken" + accessToken);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAO, "Sign in with credential: successful");
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    Log.d(TAO, "Sign in with credential: failed", task.getException());
                    Toast.makeText(Login.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {

    }

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
    private void checkAuthenticate(String uid)
    {
        DocumentReference documentReference = firestore.collection("users").document(uid);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getBoolean("isBlock") == false) {
                    if (documentSnapshot.getString("authenticate").equalsIgnoreCase(DEFAULTVALUE.ADMIN)) {
                        startActivity(new Intent(Login.this, AdminInterface.class));
                        finish();
                    } else {
                        Toast.makeText(Login.this, "Không tồn tại", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    final String msg = "Tài khoản " + documentSnapshot.getString("email") + " hiện tại đã bị khóa";
                    Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void Handlelogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassWord.getText().toString().trim();
        if (email.isEmpty()) {
            edtEmail.setError("Email is required");
            edtEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Please provide valid email!");
            edtEmail.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            edtPassWord.setError("Password is required");
            edtPassWord.requestFocus();
            return;
        }
        if (password.length() < 6) {
            edtPassWord.setError("Min password length should be 6 characters");
            edtPassWord.requestFocus();
            return;
        }
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    if (user.isEmailVerified()) {
                        checkAuthenticate(user.getUid());
                    } else {
                        user.sendEmailVerification();
                        Toast.makeText(Login.this, "Hãy xác thực email của bạn!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
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
                            Toast.makeText(Login.this, "Tài khoản không tồn tại , hãy đăng ký tài khoản", Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mFirebaseAuth.addAuthStateListener(authStateListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (authStateListener != null) {
//            mFirebaseAuth.removeAuthStateListener(authStateListener);
//        }
//    }
}
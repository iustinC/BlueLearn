package com.example.iustin.bluelearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iustin.bluelearn.R;
import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.domain.ValidationException;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RegisterActivity.class.getName();
    private static final int RC_SIGN_IN = 0;
    private static final String EMAIL = "email";

    private AccessToken lastSignedInFacebookAccount;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;

    @BindView(R.id.btnSignInFacebook)
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setupEnvironment();
        setupGoogleSignIn();
        setupFacebookSignIn();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, "User already logged with google.");
            return;
        }

        lastSignedInFacebookAccount = AccessToken.getCurrentAccessToken();
        if (lastSignedInFacebookAccount != null && !lastSignedInFacebookAccount.isExpired()) {
            Log.d(TAG, "User already logged with facebook.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, (@NonNull Task<AuthResult> task) -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Firebase sign in with google :success");
                        Intent myIntent = new Intent(RegisterActivity.this, MenuActivity.class);
                        Utils.LOGGED = true;
                        startActivity(myIntent);
                        finish();
                    } else {
                        Log.w(TAG, "Firebase sign in with google :failure", task.getException());
                    }
                });
    }

    private void firebaseAuthWithFacebook(AccessToken token) {
        Log.d(TAG, "Firebase auth with facebook:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, (@NonNull Task<AuthResult> task) -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Firebase facebook sign in :success");
                        Utils.LOGGED = true;
                    } else {
                        Log.w(TAG, "Firebase facebook sign in :failure", task.getException());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignInGoogle:
                signInGoogle();
                break;
            case R.id.btnRegister:
                register();
                break;
        }
    }

    private void setupFacebookSignIn() {
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        loginButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "Logged sucessfuly on facebook.");
                        firebaseAuthWithFacebook(loginResult.getAccessToken());
                        Intent myIntent = new Intent(RegisterActivity.this, MenuActivity.class);
                        startActivity(myIntent);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "Logging on facebook cancelled.");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "Error logging on facebook.");
                    }
                });
    }

    private void setupGoogleSignIn() {
        findViewById(R.id.btnSignInGoogle).setOnClickListener(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d(TAG, "Signing with  google.");
    }

    private void register() {
        try {
            validateInput();
            final EditText email = findViewById(R.id.editTxtEmail);
            final EditText password = findViewById(R.id.editTxtPassword);

            firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final FirebaseUser user = firebaseAuth.getCurrentUser();
                                user.sendEmailVerification()
                                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this,
                                                            "Verification email sent to " + user.getEmail(),
                                                            Toast.LENGTH_SHORT).show();
                                                    Intent myIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                                    startActivity(myIntent);
                                                    finish();
                                                } else {
                                                    Log.e(TAG, "sendEmailVerification", task.getException());
                                                    Toast.makeText(RegisterActivity.this,
                                                            "Failed to send verification email.",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                Log.d(TAG, "createUserWithEmail:success");
                            } else {
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthUserCollisionException e) {
                                    Log.w(TAG, "The email address is already in use by another account", e);
                                    Toast.makeText(RegisterActivity.this,
                                            "The email address is already in use by another account",
                                            Toast.LENGTH_LONG).show();

                                } catch (Exception e) {
                                    Log.w(TAG, "Error at creating account", e);
                                    Toast.makeText(RegisterActivity.this,
                                            "Error at creating account",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        } catch (ValidationException ex) {
            Log.w(TAG, "Exception at register", ex);
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void validateInput() throws ValidationException {
        final EditText emailValidate = findViewById(R.id.editTxtEmail);
        final EditText passwordValidate = findViewById(R.id.editTxtPassword);
        final EditText confirmPasswordVallidate = findViewById(R.id.editTxtConfirmPassword);

        if ("".equals(emailValidate.getText().toString())) {
            throw new ValidationException("Email address field is empty");
        }
        String email = emailValidate.getText().toString().trim();

        final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{4,}$";

        if (!email.matches(EMAIL_PATTERN)) {
            throw new ValidationException("Invalid email address");
        }

        if ("".equals(passwordValidate.getText().toString())) {
            throw new ValidationException("Password field is empty");
        }

        String password = passwordValidate.getText().toString().trim();

        if (!password.matches(PASSWORD_PATTERN)) {
            throw new ValidationException("A digit must occur at least once \n A lower case letter must occur at least once \n An upper case letter must occur at least once \n A special character must occur at least once you");
        }

        if ("".equals(confirmPasswordVallidate.getText().toString())) {
            throw new ValidationException("Confirm password field is empty");
        }

        String confirmPassword = confirmPasswordVallidate.getText().toString().trim();

        if (!confirmPassword.matches(PASSWORD_PATTERN)) {
            throw new ValidationException("A digit must occur at least once \n A lower case letter must occur at least once \n An upper case letter must occur at least once \n A special character must occur at least once you");
        }


        if (!passwordValidate.getText().toString().equals(confirmPasswordVallidate.getText().toString())) {
            throw new ValidationException("Passwords does not match");
        }

    }

    private void setupEnvironment() {
        final EditText emailTxtField = findViewById(R.id.editTxtEmail);
        emailTxtField.requestFocus();
    }
}

package com.example.iustin.bluelearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.iustin.bluelearn.R;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
            case R.id.btnSignOutGoogle:
                signOut();
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

    private void signOut() {
        Log.d(TAG, "Firebase sign out.");
        firebaseAuth.signOut();
    }
}

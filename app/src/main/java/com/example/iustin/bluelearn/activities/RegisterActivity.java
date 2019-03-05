package com.example.iustin.bluelearn.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

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

import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = RegisterActivity.class.getName();
    private static final int RC_SIGN_IN = 0;
    private static final String EMAIL = "email";

    private GoogleSignInAccount lastSignedInGoogleAccount;
    private AccessToken lastSignedInFacebookAccount;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;

    @BindView(R.id.btnSignInFacebook) LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setupGoogleSignIn();
        setupFacebookSignIn();
    }

    @Override
    protected void onStart() {
        super.onStart();
        lastSignedInGoogleAccount = GoogleSignIn.getLastSignedInAccount(this);
        if ( lastSignedInGoogleAccount != null) {
            Log.d(TAG, "User already logged with google.");
            return;
        }

        lastSignedInFacebookAccount = AccessToken.getCurrentAccessToken();
        if ( lastSignedInFacebookAccount != null && !lastSignedInFacebookAccount.isExpired()) {
            Log.d(TAG, "User already logged with facebook.");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInGoogleResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignInGoogle:
                signInGoogle();
                break;
            case R.id.btnSignOutGoogle:
                signOutGoogle();
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
                .requestIdToken("137471035771-56bnlpp4256vftbk39d9ga524epc8end.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void handleSignInGoogleResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d(TAG, "Result of sign in on google : " + account.getEmail());
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void signInGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        Log.d(TAG, "Signing with  google.");
    }

    private void signOutGoogle() {
        mGoogleSignInClient.signOut();
        Log.d(TAG, "Google sign out");
    }
}

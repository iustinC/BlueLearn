package com.example.iustin.bluelearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.iustin.bluelearn.R;

public class MainActivity extends AppCompatActivity {

    private Button btnRegister;
    private Button btnSignIn;
    private Button btnQuickChallenge;

    private View.OnClickListener registerOnClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener signInOnClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private View.OnClickListener quickChallengeOnClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupEnvironment();
    }

    private void setupEnvironment() {
        btnRegister = findViewById(R.id.btnRegister);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnQuickChallenge = findViewById(R.id.btnQuickChallenge);

        btnRegister.setOnClickListener(registerOnClickHandler);
        btnSignIn.setOnClickListener(signInOnClickHandler);
        btnQuickChallenge.setOnClickListener(quickChallengeOnClickHandler);

        this.getSupportActionBar().hide();
    }
}

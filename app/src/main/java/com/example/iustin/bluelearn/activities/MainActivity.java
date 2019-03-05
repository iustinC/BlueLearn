package com.example.iustin.bluelearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.iustin.bluelearn.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btnRegister) Button btnRegister;
    @BindView(R.id.btnSignIn) Button btnSignIn;
    @BindView(R.id.btnQuickChallenge) Button btnQuickChallenge;

    private View.OnClickListener registerOnClickHandler = v -> {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    };

    private View.OnClickListener signInOnClickHandler = v -> {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    };

    private View.OnClickListener quickChallengeOnClickHandler = v -> {

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupEnvironment();
    }

    private void setupEnvironment() {
        btnRegister.setOnClickListener(registerOnClickHandler);
        btnSignIn.setOnClickListener(signInOnClickHandler);
        btnQuickChallenge.setOnClickListener(quickChallengeOnClickHandler);

        this.getSupportActionBar().hide();
    }
}

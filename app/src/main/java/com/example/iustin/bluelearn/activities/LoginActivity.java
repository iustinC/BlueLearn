package com.example.iustin.bluelearn.activities;

import android.os.Bundle;
import android.util.Log;

import com.example.iustin.bluelearn.R;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.getSupportActionBar().hide();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "On start login activity");
    }

}

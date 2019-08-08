package com.example.iustin.bluelearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iustin.bluelearn.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        this.getSupportActionBar().hide();
    }

    public void btnQuickChallenge(View view) {
        Intent intent = new Intent(MenuActivity.this, QuizActivity.class);
        startActivity(intent);
    }
}

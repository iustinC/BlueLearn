package com.example.iustin.bluelearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iustin.bluelearn.R;
import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.repository.CategoryRepository;

import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {


    private CategoryRepository categoryRepository = new CategoryRepository(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Utils.categoriesNames = new ArrayList<>();
        Utils.selectedCategory = "";

        categoryRepository.loadCategoriesName();
        this.getSupportActionBar().hide();
    }

    public void btnQuickChallenge(View view) {
        Intent intent = new Intent(MenuActivity.this, QuizActivity.class);
        intent.putExtra("caller", "withDifficulty");
        startActivity(intent);
    }

    public void btnStartCourse(View view) {
        Intent intent = new Intent(MenuActivity.this, CategoryActivity.class);
        startActivity(intent);
    }

    public void btnSendQuestion(View view) {
        Intent intent = new  Intent(MenuActivity.this, SendEmailQuestionActivity.class);
        startActivity(intent);
    }

    public void sendFeedback(View view) {

    }
}

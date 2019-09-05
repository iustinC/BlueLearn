package com.example.iustin.bluelearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iustin.bluelearn.R;

import java.util.ArrayList;
import java.util.List;

public class SendEmailQuestionActivity extends AppCompatActivity {

    private EditText statement;
    private EditText answer1;
    private EditText answer2;
    private EditText answer3;
    private EditText answer4;
    private EditText rightAnswer;
    private Spinner questionType;
    private Spinner questionDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email_question);

        statement = (EditText)findViewById(R.id.txtStatement);
        answer1 = (EditText)findViewById(R.id.txtAnswer1);
        answer2 = (EditText)findViewById(R.id.txtAnswer2);
        answer3 = (EditText)findViewById(R.id.txtAnswer3);
        answer4 = (EditText)findViewById(R.id.txtAnswer4);
        rightAnswer = (EditText) findViewById(R.id.txtRightAnswer);
        questionType = findViewById(R.id.spinnerQuestionType);
        questionDifficulty = findViewById(R.id.spinnerQuestionDifficulty);


        List<String> difficulty = new ArrayList<>();
        difficulty.add("EASY");
        difficulty.add("MEDIUM");
        difficulty.add("HARD");

        List<String> type = new ArrayList<>();
        type.add("CHOICE_ANSWER");
        type.add("TEXT_ANSWER");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, difficulty);
        questionDifficulty.setAdapter(adapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, type);
        questionType.setAdapter(typeAdapter);
    }

    public void sendQuestion(View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"iustincristian1738@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "New Question request");
        i.putExtra(Intent.EXTRA_TEXT   , "Hello, \n" +
                " I have a suggestion:\n"+
                "Statement: " + statement.getText().toString() + "\n"+
                "Answer 1: " + answer1.getText().toString() + "\n" +
                "Answer 2: " + answer2.getText().toString() + "\n" +
                "Answer 3: " + answer3.getText().toString() + "\n" +
                "Answer 4: " + answer4.getText().toString() + "\n" +
                "Right answer: " + rightAnswer.getText().toString() + "\n" +
                "Question type: " + questionType.getSelectedItem().toString() + "\n" +
                "Difficulty type: " + questionDifficulty.getSelectedItem().toString());
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendEmailQuestionActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}

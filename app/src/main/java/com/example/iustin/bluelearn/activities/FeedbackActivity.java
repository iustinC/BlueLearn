package com.example.iustin.bluelearn.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iustin.bluelearn.R;

public class FeedbackActivity extends AppCompatActivity {

    private TextView txtFeedback;
    private EditText editTextFeedback;
    private Button sendFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        txtFeedback = findViewById(R.id.txtViewFeedback);
        editTextFeedback = findViewById(R.id.editTextFeedback);
        sendFeedback = findViewById(R.id.btnSendFeedback);
    }

    public void sendFeedbackEmail(View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"iustincristian1738@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
        i.putExtra(Intent.EXTRA_TEXT   , "Hello, \n" +
                "My feedback about your application: \n " + editTextFeedback.getText().toString());
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FeedbackActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }finally {
            finish();
        }
    }
}

package com.example.iustin.bluelearn.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iustin.bluelearn.R;

public class ResultActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txt_timer;
    TextView txt_result;
    TextView txt_right_answer;
    Button btn_filter_total;
    Button btn_filter_right;
    Button btn_filter_wrong;
    Button btn_filter_no_answer;
    RecyclerView recycler_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("RESULT");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txt_result = (TextView) findViewById(R.id.txt_result);
        txt_right_answer = (TextView) findViewById(R.id.txt_right_answer);
        txt_timer = (TextView) findViewById(R.id.txt_time);


        btn_filter_no_answer =  findViewById(R.id.btn_filter_no_answer);
        btn_filter_right =  findViewById(R.id.btn_filter_right_answer);
        btn_filter_total =  findViewById(R.id.btn_filter_total);
        btn_filter_wrong =  findViewById(R.id.btn_filter_wrong_answer);

        recycler_result = findViewById(R.id.recycler_result);
        recycler_result.setHasFixedSize(true);
        recycler_result.setLayoutManager(new GridLayoutManager(this, 3));

        if ( ) {
        }
    }
}

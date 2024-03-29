package com.example.iustin.bluelearn.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iustin.bluelearn.R;
import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.adapters.ResultGridAdapter;
import com.example.iustin.bluelearn.domain.AnswerType;
import com.example.iustin.bluelearn.repository.LeaderboardRepository;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import java.util.concurrent.TimeUnit;


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
    Button btnAddToLeaderboard;
    EditText playerName;
    Button viewLeaderboard;

    private LeaderboardRepository leaderboardRepository = new LeaderboardRepository(this);

    ResultGridAdapter adapter, filtered_adapter;

    BroadcastReceiver backToQuestion = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().toString().equals(Utils.GET_BACK_FROM_RESULT)) {
                int question = intent.getIntExtra(Utils.GET_BACK_FROM_RESULT, -1);
                goBackActivityWithQuestion(question);
            }
        }
    };

    private void goBackActivityWithQuestion(int question) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Utils.GET_BACK_FROM_RESULT, question);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Utils.checkCurrentForm();

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(backToQuestion, new IntentFilter(Utils.GET_BACK_FROM_RESULT));

        toolbar = (Toolbar) findViewById(R.id.toolbar_result);
        toolbar.setTitle("RESULT");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        playerName = findViewById(R.id.editTextPlayerName);
        viewLeaderboard = findViewById(R.id.btnViewLeaderboard);


        viewLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaderboardRepository.loadLeaderboard();
            }
        });

        btnAddToLeaderboard = findViewById(R.id.btnAddToLeaderboard);
        btnAddToLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utils.selectedCategory.isEmpty()) {
                    if (Utils.LOGGED) {
                        leaderboardRepository.addNewScore(String.valueOf(Utils.percentageOfResult * TimeUnit.MILLISECONDS.toSeconds(Utils.timer)) , Utils.currentUser);
                    } else {
                        leaderboardRepository.addNewScore(String.valueOf(Utils.percentageOfResult * TimeUnit.MILLISECONDS.toSeconds(Utils.timer)) , playerName.getText().toString());
                    }
                }
            }
        });

        if (Utils.LOGGED) {
            playerName.setVisibility(View.INVISIBLE);
        }

        if (Utils.selectedCategory.isEmpty()) {
            playerName.setVisibility(View.INVISIBLE);
            btnAddToLeaderboard.setVisibility(View.INVISIBLE);
        }

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

        adapter = new ResultGridAdapter(this, Utils.currentQuestions);
        recycler_result.setAdapter(adapter);

        txt_timer.setText(String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(Utils.timer),
                (TimeUnit.MILLISECONDS.toSeconds(Utils.timer) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Utils.timer)))));
        txt_right_answer.setText(new StringBuilder("").append(Utils.CORRECT_ANSWER_COUNT).append("/").append(Utils.currentQuestions.size()));

        btn_filter_total.setText(new StringBuilder("").append(Utils.currentQuestions.size()));
        btn_filter_wrong.setText(new StringBuilder("").append(Utils.WRONG_ANSWER_COUNT));
        btn_filter_right.setText(new StringBuilder("").append(Utils.CORRECT_ANSWER_COUNT));
        btn_filter_no_answer.setText(new StringBuilder("").append(Utils.currentQuestions.size() - Utils.selectedValues.keySet().size()));

         Utils.percentageOfResult = ((Utils.CORRECT_ANSWER_COUNT * 100) / Utils.currentQuestions.size());
        if (Utils.percentageOfResult >= 90) {
            txt_result.setText("VERY GOOD");
        } else if (Utils.percentageOfResult >= 60){
            txt_result.setText("GOOD");
        } else if (Utils.percentageOfResult >= 40) {
            txt_result.setText("BAD");
        } else {
            txt_result.setText("FAIL");
        }

        btn_filter_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter == null) {
                    adapter = new ResultGridAdapter(ResultActivity.this, Utils.currentQuestions);
                    recycler_result.setAdapter(adapter);
                } else {
                    recycler_result.setAdapter(adapter);
                }
            }
        });

        btn_filter_no_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.filteredQuestions.clear();
                for (int i = 0 ; i < Utils.currentQuestions.size() ; i++) {
                    if (Utils.getAnswerOfQuestion(i, Utils.currentQuestions) == AnswerType.NO_ANSWER) {
                        Utils.filteredQuestions.add(Utils.currentQuestions.get(i));
                    }
                }
                filtered_adapter = new ResultGridAdapter(ResultActivity.this, Utils.filteredQuestions);
                recycler_result.setAdapter(filtered_adapter);
            }
        });

        btn_filter_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.filteredQuestions.clear();
                for (int i = 0 ; i < Utils.currentQuestions.size() ; i++) {
                    if (Utils.getAnswerOfQuestion(i, Utils.currentQuestions) == AnswerType.CORRECT) {
                        Utils.filteredQuestions.add(Utils.currentQuestions.get(i));
                    }
                }
                filtered_adapter = new ResultGridAdapter(ResultActivity.this, Utils.filteredQuestions);
                recycler_result.setAdapter(filtered_adapter);
            }
        });

        btn_filter_wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.filteredQuestions.clear();
                for (int i = 0 ; i < Utils.currentQuestions.size() ; i++) {
                    if (Utils.getAnswerOfQuestion(i, Utils.currentQuestions) == AnswerType.WRONG) {
                        Utils.filteredQuestions.add(Utils.currentQuestions.get(i));
                    }
                }
                filtered_adapter = new ResultGridAdapter(ResultActivity.this, Utils.filteredQuestions);
                recycler_result.setAdapter(filtered_adapter);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.result_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_do_quiz_again:
                doQuizAgain();
                break;
            case R.id.menu_view_answer:
                viewQuizAnswer();
                break;
            case android.R.id.home:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void viewQuizAnswer() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("action", "viewquizansswer");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }


    private void doQuizAgain() {
        new MaterialStyledDialog.Builder(ResultActivity.this)
                .setTitle("Do quiz again?")
                .setIcon(R.drawable.ic_videogame_asset_black_24dp)
                .setDescription("Do you want to start quiz again?")
                .setPositiveText("Yes")
                .setNegativeText("No")
                .onNegative((dialog, which) -> {
                    dialog.dismiss();
                })
                .onPositive((dialog, which) -> {
                    dialog.dismiss();

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("action", "doitagain");
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                })
                .show();
    }

    public void goToLeaderboard() {
        Intent intent = new Intent(ResultActivity.this, LeaderboardActivity.class);
        startActivity(intent);
    }
}

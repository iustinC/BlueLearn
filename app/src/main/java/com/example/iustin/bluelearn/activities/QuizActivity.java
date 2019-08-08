package com.example.iustin.bluelearn.activities;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.iustin.bluelearn.R;
import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.adapters.QuestionFragmentAdapter;
import com.example.iustin.bluelearn.domain.DifficultyType;
import com.example.iustin.bluelearn.domain.Question;
import com.example.iustin.bluelearn.domain.QuestionType;
import com.example.iustin.bluelearn.fragments.QuestionFragment;
import com.example.iustin.bluelearn.repository.QuestionsRepository;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class QuizActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, QuestionFragment.OnFragmentInteractionListener {

    private static final String TAG = QuizActivity.class.getName();

    private int timerDuration = Utils.TIME_FOR_QUIZ;
    boolean isAnswerModeView = false;
    private QuestionsRepository questionsRepository = new QuestionsRepository(this);
    private TextView txtTimer;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ProgressDialog dialog;

    @Override
    protected void onDestroy() {
        if (Utils.TIMER != null) {
            Utils.TIMER.cancel();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupEnvironment();
        questionsRepository.getDatabase().child("/easy").child("/1").setValue(new Question(1,"Intrebare?", "r1", "r2", "r3", "r4", "rc", DifficultyType.EASY, QuestionType.CHOICE_ANSWER));
        questionsRepository.getDatabase().child("/medium").child("/1").setValue(new Question(1,"Intrebare?", "r1", "r2", "r3", "r4", "rc", DifficultyType.MEDIUM, QuestionType.CHOICE_ANSWER));
        questionsRepository.getDatabase().child("/hard").child("/1").setValue(new Question(1,"Intrebare?", "r1", "r2", "r3", "r4", "rc", DifficultyType.HARD, QuestionType.CHOICE_ANSWER));
    }

    private void setupEnvironment() {
        txtTimer = (TextView) findViewById(R.id.txtTimer);
        txtTimer.setVisibility(View.VISIBLE);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.slidingTabs);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading data, please wait.");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        questionsRepository.getRandomQuestions(10);

    }

    public void loadData() {
        genFragmentList();

        QuestionFragmentAdapter questionFragmentAdapter = new QuestionFragmentAdapter(getSupportFragmentManager(), this, Utils.listFramgments);
        viewPager.setAdapter(questionFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void stopLoadingScreen() {
        dialog.hide();
        countTimer();
    }

    private void genFragmentList() {
        for (int i = 0 ; i < questionsRepository.getCurrentQuestions().size() ; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("index", i);
            QuestionFragment questionFragment = new QuestionFragment();
            questionFragment.setArguments(bundle);

            Utils.listFramgments.add(questionFragment);
;        }
    }

    private void countTimer() {
        if (Utils.TIMER == null) {
            Utils.TIMER = new CountDownTimer(Utils.TIME_FOR_QUIZ, 1000) {
                @Override
                public void onTick(long l) {
                    txtTimer.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(l),
                            TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))));

                    timerDuration -= 10000;
                }

                @Override
                public void onFinish() {

                }
            }.start();
        } else {
            Utils.TIMER.cancel();
            Utils.TIMER = new CountDownTimer(Utils.TIME_FOR_QUIZ, 1000) {
                @Override
                public void onTick(long l) {
                    txtTimer.setText(String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(l),
                            TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l))));

                    timerDuration -= 10000;
                }

                @Override
                public void onFinish() {

                }
            }.start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_finish_game) {
            if (!isAnswerModeView) {
                new MaterialStyledDialog.Builder(this)
                        .setTitle("Finish game?")
                        .setIcon(R.drawable.ic_videogame_asset_black_24dp)
                        .

            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

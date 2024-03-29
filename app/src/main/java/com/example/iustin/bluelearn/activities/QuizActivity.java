package com.example.iustin.bluelearn.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.iustin.bluelearn.R;
import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.adapters.QuestionFragmentAdapter;
import com.example.iustin.bluelearn.fragments.DifficultyFragment;
import com.example.iustin.bluelearn.fragments.QuestionFragment;
import com.example.iustin.bluelearn.repository.QuestionsRepository;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.concurrent.TimeUnit;

public class QuizActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, QuestionFragment.OnFragmentInteractionListener,
                    DifficultyFragment.OnFragmentInteractionListener{

    private static final String TAG = QuizActivity.class.getName();

    private int timerDuration = Utils.TIME_FOR_QUIZ;
    boolean isAnswerModeView = false;
    private QuestionsRepository questionsRepository = new QuestionsRepository(this);
    private TextView txtTimer;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private ProgressDialog dialog;
    private QuestionFragmentAdapter questionFragmentAdapter;

    private static final int CODE_GET_RESULT = 9999;

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

        toolbar = findViewById(R.id.toolbar);
        String caller = getIntent().getStringExtra("caller");
        if ("withDifficulty".equals(caller)) {
            toolbar.setTitle("Choose difficulty");
            setSupportActionBar(toolbar);
            chooseDifficulty();
        } else if ("course".equals(caller)) {
            toolbar.setTitle(Utils.selectedCategory);
            setSupportActionBar(toolbar);
            startQuizCourse();
        }
    }

    private void chooseDifficulty() {
        DifficultyFragment difficultyFragment = new DifficultyFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.difficulty_fragment, difficultyFragment).commit();
    }

    public void startQuiz() {
        toolbar.setTitle("Good luck");
        txtTimer = (TextView) findViewById(R.id.txtTimer);
        txtTimer.setVisibility(View.VISIBLE);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.slidingTabs);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading data, please wait.");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        questionsRepository.loadQuestions();

    }

    public void startQuizCourse() {
        txtTimer = (TextView) findViewById(R.id.txtTimer);
        txtTimer.setVisibility(View.VISIBLE);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.slidingTabs);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading data, please wait.");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();
        questionsRepository.loadCourseQuestions();
    }

    public void loadData() {
        genFragmentList();

        questionFragmentAdapter = new QuestionFragmentAdapter(getSupportFragmentManager(), this, Utils.listFramgments);

        viewPager.setAdapter(questionFragmentAdapter);
        viewPager.setOffscreenPageLimit(Utils.listFramgments.size());
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int SCROLLING_RIGHT = 0;
            int SCROLLING_LEFT = 1;
            int SCROLLING_UNDETERMINED = 2;

            int currentScrollDirection = 2;

            private void setScrollingDirection(float positionOffset) {
                if (1 - positionOffset >= 0.5) {
                    this.currentScrollDirection = SCROLLING_RIGHT;
                } else if (1 - positionOffset <= 0.5) {
                    this.currentScrollDirection = SCROLLING_LEFT;
                }
            }

            private boolean isScrollDirectionUndetermined() {
                return currentScrollDirection == SCROLLING_UNDETERMINED;
            }

            private boolean isScrollDirectionRight() {
                return currentScrollDirection == SCROLLING_RIGHT;
            }

            private boolean isScrollDirectionLeft() {
                return currentScrollDirection == SCROLLING_LEFT;
            }


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (!isScrollDirectionUndetermined()) {
                    setScrollingDirection(positionOffset);
                }

                InputMethodManager imm = (InputMethodManager) QuizActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }

            @Override
            public void onPageSelected(int position) {
                QuestionFragment questionFragment;
                int currentPosition = 0;

                if (position > 0) {
                    if (isScrollDirectionRight()) {
                        questionFragment = Utils.listFramgments.get(position - 1);
                        currentPosition = position - 1;
                    } else if (isScrollDirectionLeft()) {
                        questionFragment = Utils.listFramgments.get(position + 1);
                        currentPosition = position +1;
                    } else {
                        questionFragment = Utils.listFramgments.get(currentPosition);
                    }
                } else {
                    questionFragment = Utils.listFramgments.get(0);
                    currentPosition = 0;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    this.currentScrollDirection = SCROLLING_UNDETERMINED;
                }
            }
        });
    }

    public void stopLoadingScreen() {
        dialog.hide();
        countTimer();
    }

    private void genFragmentList() {
        if (!Utils.listFramgments.isEmpty()) {
            Utils.listFramgments.clear();
        }
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

                    timerDuration -= 1000;
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

                    timerDuration -= 1000;
                }

                @Override
                public void onFinish() {

                }
            }.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    private void finishGame() {
        Intent intent = new Intent(QuizActivity.this, ResultActivity.class);
        Utils.timer = Utils.TIME_FOR_QUIZ - timerDuration;
        startActivityForResult(intent, CODE_GET_RESULT);
        for (QuestionFragment listFramgment : Utils.listFramgments) {
            listFramgment.showCorrectAnswer();
            listFramgment.disableAnswer();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        //TODO: IF U PRESS FINISH ON ANSWERMODEVIEW GET BACK TO THE MENU
        if (id == R.id.menu_finish_game) {
            if (!isAnswerModeView) {
                new MaterialStyledDialog.Builder(this)
                        .setTitle("Finish game?")
                        .setIcon(R.drawable.ic_videogame_asset_black_24dp)
                        .setDescription("Are you sure you want to finish the game?")
                        .setPositiveText("Yes, totally")
                        .setNegativeText("No")
                        .onNegative((dialog, which) -> {
                            dialog.dismiss();
                        })
                        .onPositive((dialog, which) -> {
                            dialog.dismiss();
                            Utils.checkCurrentForm();
                            finishGame();
                        })
                        .show();

            } else {
                dialog.dismiss();
                if (Utils.LOGGED) {
                    Intent myIntent = new Intent(QuizActivity.this, MenuActivity.class);

                    startActivity(myIntent);
                    finish();
                } else {
                    Intent myIntent = new Intent(QuizActivity.this, MainActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "====================== ON ACTIVITY RESULT CALLED ====================" + "rc: " + requestCode + " resultcode: " + resultCode + " intent: " + data);
        if (requestCode == CODE_GET_RESULT) {
            if (resultCode == Activity.RESULT_OK) {
                String action = data.getStringExtra("action");
                if (action == null || TextUtils.isEmpty(action)) {
                    int questionNumber = data.getIntExtra(Utils.GET_BACK_FROM_RESULT, -1);
                    viewPager.setCurrentItem(questionNumber);

                    isAnswerModeView = true;
                    Utils.TIMER.cancel();

                    txtTimer.setVisibility(View.GONE);

                    Log.d(TAG, "ACTION ON QUIZ ACTIVITY");
                } else {
                    if (action.equals("viewquizansswer")) {
                        viewPager.setCurrentItem(0);

                        isAnswerModeView = true;
                        Utils.TIMER.cancel();

                        txtTimer.setVisibility(View.GONE);

                        for (QuestionFragment questionFragment : Utils.listFramgments) {
                            questionFragment.showCorrectAnswer();
                            questionFragment.disableAnswer();
                        }

                        Log.d(TAG, "view quiz answer ON QUIZ ACTIVITY");
                    } else if (action.equals("doitagain")) {
                        viewPager.setCurrentItem(0);

                        isAnswerModeView = false;
                        countTimer();

                        txtTimer.setVisibility(View.VISIBLE);
                        Utils.selectedValues.clear();

                        for (QuestionFragment questionFragment : Utils.listFramgments) {
                            questionFragment.resetQuestion();
                        }

                        Utils.WRONG_ANSWER_COUNT = 0;
                        Utils.CORRECT_ANSWER_COUNT = 0;
                        Utils.NO_ANSWER_COUNT = 0;

                        Log.d(TAG, "do it again ON QUIZ ACTIVITY");
                    }
                }
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

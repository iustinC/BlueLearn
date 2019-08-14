package com.example.iustin.bluelearn.activities;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.iustin.bluelearn.R;
import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.adapters.QuestionFragmentAdapter;
import com.example.iustin.bluelearn.domain.DifficultyType;
import com.example.iustin.bluelearn.domain.Question;
import com.example.iustin.bluelearn.domain.QuestionType;
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
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        chooseDifficulty();
        }

    private void chooseDifficulty() {
        DifficultyFragment difficultyFragment = new DifficultyFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.difficulty_fragment, difficultyFragment).commit();
    }

    public void startQuiz() {
        questionsRepository.getDatabase().child("/easy").child("/1").setValue(new Question(1,"Intrebare?", "r1", "r2", "r3", "r4", "rc", DifficultyType.Easy, QuestionType.CHOICE_ANSWER));
        questionsRepository.getDatabase().child("/medium").child("/1").setValue(new Question(1,"Intrebare?", "r1", "r2", "r3", "r4", "rc", DifficultyType.Medium, QuestionType.CHOICE_ANSWER));
        questionsRepository.getDatabase().child("/hard").child("/1").setValue(new Question(1,"Intrebare?", "r1", "r2", "r3", "r4", "rc", DifficultyType.Hard, QuestionType.CHOICE_ANSWER));

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.quiz, menu);
        return true;
    }

    private void finishGame() {
        int position = viewPager.getCurrentItem();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

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
                            finishGame();
                        })
                        .show();

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

package com.example.iustin.bluelearn.repository;

import androidx.annotation.NonNull;

import com.example.iustin.bluelearn.activities.QuizActivity;
import com.example.iustin.bluelearn.domain.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionsRepository {


    private static final String TAG = QuestionsRepository.class.getName();

    private static List<Question> currentQuestions;

    private DatabaseReference database;

    private QuizActivity quizActivity;

    {
        database = FirebaseDatabase.getInstance().getReference().child("/questions");
    }

    public QuestionsRepository(List<Question> currentQuestions) {
        this.currentQuestions = currentQuestions;
    }

    public QuestionsRepository(QuizActivity quizActivity) {
        this.quizActivity = quizActivity;
    }

    public void getRandomQuestions(int numberOfQuestions) {
        List<Question> result = new ArrayList<>();
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int countEasyQuestions = new Random().nextInt(numberOfQuestions);
                int countMediumQuestions =  new Random().nextInt( numberOfQuestions - countEasyQuestions);
                int countHardQuestions = numberOfQuestions - countMediumQuestions - countEasyQuestions;

                List<Question> easyQuestions = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.child("/easy").getChildren()) {
                    easyQuestions.add(data.getValue(Question.class));
                }

                List<Question> mediumQuestions = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.child("/medium").getChildren()) {
                    mediumQuestions.add(data.getValue(Question.class));
                }

                List<Question> hardQuestions = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.child("/hard").getChildren()) {
                    hardQuestions.add(data.getValue(Question.class));
                }

                for (int i = 0; i < countEasyQuestions ; i++) {
                    result.add(easyQuestions.get(easyQuestions.size() == 1 ?  0 : new Random().nextInt(easyQuestions.size())));
                }

                for (int i = 0; i < countHardQuestions ; i++) {
                    result.add(hardQuestions.get(hardQuestions.size() == 1 ? 0 : new Random().nextInt(hardQuestions.size())));
                }

                for (int i = 0; i < countMediumQuestions ; i++) {
                    result.add(mediumQuestions.get(mediumQuestions.size() == 1 ? 0 : new Random().nextInt(mediumQuestions.size())));
                }

                currentQuestions = result;
                quizActivity.loadData();
                quizActivity.stopLoadingScreen();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    public static Question getQuestionByIndex(int index) {
        return currentQuestions.get(index);
    }

    public DatabaseReference getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseReference database) {
        this.database = database;
    }

    public List<Question> getCurrentQuestions() {
        return currentQuestions;
    }

    public void setCurrentQuestions(List<Question> currentQuestions) {
        this.currentQuestions = currentQuestions;
    }
}

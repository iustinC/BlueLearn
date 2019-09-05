package com.example.iustin.bluelearn.repository;

import androidx.annotation.NonNull;

import com.example.iustin.bluelearn.Utils;
import com.example.iustin.bluelearn.activities.QuizActivity;
import com.example.iustin.bluelearn.domain.DifficultyType;
import com.example.iustin.bluelearn.domain.Question;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuestionsRepository {


    private static final String TAG = QuestionsRepository.class.getName();

    private DatabaseReference database;

    private QuizActivity quizActivity;

    {
        database = FirebaseDatabase.getInstance().getReference().child("/questions");
    }

    public QuestionsRepository(QuizActivity quizActivity) {
        this.quizActivity = quizActivity;
    }

    public void loadCourseQuestions() {
        Utils.currentQuestions = new ArrayList<>();
        Utils.selectedValues.clear();
        List<Question> result = new ArrayList<>();
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot data : dataSnapshot.child("/course/" + Utils.selectedCategory).getChildren()) {
                    result.add(data.getValue(Question.class));
                }
                Utils.currentQuestions = result;
                quizActivity.loadData();
                quizActivity.stopLoadingScreen();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void loadQuestions() {
        Utils.currentQuestions = new ArrayList<>();
        Utils.selectedValues.clear();
        List<Question> result = new ArrayList<>();
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int countEasyQuestions = DifficultyType.Random.equals(Utils.selectedDifficulty) ? new Random().nextInt(Utils.DEFAULT_QUESTIONS_COUNT) : Utils.DEFAULT_QUESTIONS_COUNT;
                int countMediumQuestions =  DifficultyType.Random.equals(Utils.selectedDifficulty) ? new Random().nextInt( Utils.DEFAULT_QUESTIONS_COUNT - countEasyQuestions) : Utils.DEFAULT_QUESTIONS_COUNT;
                int countHardQuestions = DifficultyType.Random.equals(Utils.selectedDifficulty) ? Utils.DEFAULT_QUESTIONS_COUNT - countMediumQuestions - countEasyQuestions : Utils.DEFAULT_QUESTIONS_COUNT;
                Set<Question> easyQuestions = new HashSet<>();
                Set<Question> mediumQuestions = new HashSet<>();
                Set<Question> hardQuestions = new HashSet<>();

                switch (Utils.selectedDifficulty){
                    case Random:
                        for(DataSnapshot data : dataSnapshot.child("/easy").getChildren()) {
                            easyQuestions.add(data.getValue(Question.class));
                        }

                        for(DataSnapshot data : dataSnapshot.child("/medium").getChildren()) {
                            mediumQuestions.add(data.getValue(Question.class));
                        }

                        for(DataSnapshot data : dataSnapshot.child("/hard").getChildren()) {
                            hardQuestions.add(data.getValue(Question.class));
                        }

                        for (int i = 0; i < countEasyQuestions ; i++) {
                            result.add(easyQuestions.iterator().next());
                        }

                        for (int i = 0; i < countHardQuestions ; i++) {
                            result.add(hardQuestions.iterator().next());
                        }

                        for (int i = 0; i < countMediumQuestions ; i++) {
                            result.add(mediumQuestions.iterator().next());
                        }
                        break;
                    case Easy:
                        for(DataSnapshot data : dataSnapshot.child("/easy").getChildren()) {
                            easyQuestions.add(data.getValue(Question.class));
                        }

                        result.addAll(easyQuestions);

                        break;
                    case Medium:
                        for(DataSnapshot data : dataSnapshot.child("/medium").getChildren()) {
                            mediumQuestions.add(data.getValue(Question.class));
                        }

                       result.addAll(mediumQuestions);

                        break;
                    case Hard:
                        for(DataSnapshot data : dataSnapshot.child("/hard").getChildren()) {
                            hardQuestions.add(data.getValue(Question.class));
                        }

                        result.addAll(hardQuestions);

                        break;
                }

                Utils.currentQuestions = result;
                quizActivity.loadData();
                quizActivity.stopLoadingScreen();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }

    public static Question getQuestionByIndex(int index) {
        return Utils.currentQuestions.get(index);
    }

    public DatabaseReference getDatabase() {
        return database;
    }

    public void setDatabase(DatabaseReference database) {
        this.database = database;
    }

    public List<Question> getCurrentQuestions() {
        return Utils.currentQuestions;
    }

    public void setCurrentQuestions(List<Question> currentQuestions) {
        Utils.currentQuestions = currentQuestions;
    }
}

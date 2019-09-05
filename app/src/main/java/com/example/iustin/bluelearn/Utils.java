package com.example.iustin.bluelearn;

import android.os.CountDownTimer;

import com.example.iustin.bluelearn.domain.AnswerType;
import com.example.iustin.bluelearn.domain.CourseSlide;
import com.example.iustin.bluelearn.domain.DifficultyType;
import com.example.iustin.bluelearn.domain.Question;
import com.example.iustin.bluelearn.fragments.CourseFragment;
import com.example.iustin.bluelearn.fragments.QuestionFragment;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Utils {

    public static List<Question> currentQuestions;
    public static List<Question> filteredQuestions = new ArrayList<>();

    public static final String GET_BACK_FROM_RESULT = "BACK_FROM_RESULT";
    public static int TIME_FOR_QUIZ = 20 * 60 * 1000;
    public static CountDownTimer TIMER;

    public static List<String> categoriesNames = new ArrayList<>();
    public static String selectedCategory = "";
    public static List<CourseSlide> courseSlides = new ArrayList<>();

    public static List<CourseFragment> courseFragments = new ArrayList<>();


    public static boolean LOGGED = false;
    public static int timer = 0;
    public static StringBuilder data_question = new StringBuilder();

    public static List<QuestionFragment> listFramgments = new ArrayList<>();
    public static MultiValuedMap<String, String> selectedValues = new ArrayListValuedHashMap<>();

    public static DifficultyType selectedDifficulty;

    public static int NO_ANSWER_COUNT;
    public static int CORRECT_ANSWER_COUNT;
    public static int WRONG_ANSWER_COUNT;
    public static int DEFAULT_QUESTIONS_COUNT = 10;

    public static void checkCurrentForm() {
        Utils.WRONG_ANSWER_COUNT = 0;
        Utils.CORRECT_ANSWER_COUNT = 0;
        Utils.NO_ANSWER_COUNT = 0;
        for (String key : selectedValues.keySet()) {
            Question question = getQuestionById(Integer.parseInt(key));
            Set<String> answers = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
            answers.addAll(selectedValues.get(question.getId().toString()));

            Set<String> correctAnswers = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
            correctAnswers.addAll(Arrays.asList(question.getRightAnswer().split(",")));

            boolean correctAnswerForCurrentQuetion = false;
            if (answers.equals(correctAnswers)) {
                correctAnswerForCurrentQuetion = true;
            }
            if (correctAnswerForCurrentQuetion) {
                CORRECT_ANSWER_COUNT++;
            } else if (answers.isEmpty()){
                NO_ANSWER_COUNT++;
            } else {
                WRONG_ANSWER_COUNT++;
            }
        }
    }

    public static Collection<String> getAnswersOfQuestion(int idOfQuestion) {
        return selectedValues.get(String.valueOf(idOfQuestion));
    }

    public static AnswerType getAnswerOfQuestion(int i, List<Question> questions) {
        Question question = questions.get(i);

        Set<String> answers = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        answers.addAll(selectedValues.get(question.getId().toString()));

        Set<String> correctAnswers = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        correctAnswers.addAll(Arrays.asList(question.getRightAnswer().split(",")));
        if (answers.isEmpty()) {
            return AnswerType.NO_ANSWER;
        }

        if (answers.equals(correctAnswers)) {
            return AnswerType.CORRECT;
        }

        return AnswerType.WRONG;
    }

    private static Question getQuestionById(int id){
        return currentQuestions.stream()
                .filter(question -> question.getId().equals(id))
                .findFirst().get();
    }

}

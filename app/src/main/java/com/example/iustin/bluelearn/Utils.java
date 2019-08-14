package com.example.iustin.bluelearn;

import android.os.CountDownTimer;

import com.example.iustin.bluelearn.domain.DifficultyType;
import com.example.iustin.bluelearn.fragments.QuestionFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Utils {

    public static int TIME_FOR_QUIZ = 20 * 60 * 1000;
    public static CountDownTimer TIMER;

    public static List<QuestionFragment> listFramgments = new ArrayList<>();
    public static Set<String> selectedValues = new TreeSet<>();

    public static DifficultyType selectedDifficulty;

    public static int CORRECT_ANSWER_COUNT = 0;
    public static int WRONG_ANSWER_COUNT = 0;
}

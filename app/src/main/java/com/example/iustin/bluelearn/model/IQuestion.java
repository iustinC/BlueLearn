package com.example.iustin.bluelearn.model;

import com.example.iustin.bluelearn.domain.Question;

public interface IQuestion {
    Question getSelectedAnswer();
    void showCorrectAnswer();
    void disableAnswer();
    void resetQuestion();
}

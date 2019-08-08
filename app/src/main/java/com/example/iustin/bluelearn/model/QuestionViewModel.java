package com.example.iustin.bluelearn.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.iustin.bluelearn.domain.Question;

import java.util.List;

public class QuestionViewModel extends ViewModel {
    private MutableLiveData<List<Question>> questions;

    public LiveData<List<Question>> getArticles() {
        if (questions == null) {
            questions = new MutableLiveData<List<Question>>();
            loadQuestions();
        }
        return questions;
    }

    private void loadQuestions() {

    }
}

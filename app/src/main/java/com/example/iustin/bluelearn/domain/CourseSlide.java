package com.example.iustin.bluelearn.domain;

public class CourseSlide {

    public String statement;

    public CourseSlide(String statement) {
        this.statement = statement;
    }

    public CourseSlide() {
    }


    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
}


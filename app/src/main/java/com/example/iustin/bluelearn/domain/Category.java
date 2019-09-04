package com.example.iustin.bluelearn.domain;

public class Category {

    public String statement;
    public String name;


    public Category(String statement, String name) {
        this.statement = statement;
        this.name = name;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

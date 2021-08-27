package com.example.a100daysofcode;

import java.io.Serializable;

public class Problem implements Serializable {
    String title;
    String statement;
    String difficulty;
    int status;
    int submissions;
    String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int isStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSubmissions() {
        return submissions;
    }

    public void setSubmissions(int submissions) {
        this.submissions = submissions;
    }

    public Problem(String title, String statement, String answer,String difficulty, int status) {
        this.title = title;
        this.statement = statement;
        this.difficulty = difficulty;
        this.status = status;
        this.answer = answer;
    }
}

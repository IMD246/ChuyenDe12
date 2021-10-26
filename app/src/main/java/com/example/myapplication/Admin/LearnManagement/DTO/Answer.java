package com.example.myapplication.Admin.LearnManagement.DTO;

public class Answer {
    private int id;
    private String answerQuestion,urlImage = "";

    public Answer(int id, String answerQuestion, String urlImage) {
        this.id = id;
        this.answerQuestion = answerQuestion;
        this.urlImage = urlImage;
    }

    public Answer() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnswerQuestion() {
        return answerQuestion;
    }

    public void setAnswerQuestion(String answerQuestion) {
        this.answerQuestion = answerQuestion;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}

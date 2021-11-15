package com.example.EnglishBeginner.DTO;

public class Answer {
    private int id;
    private String answerQuestion,urlImage = "";
    private boolean check = false;

    public Answer(int id, String answerQuestion, String urlImage) {
        this.id = id;
        this.answerQuestion = answerQuestion;
        this.urlImage = urlImage;
    }

    public Answer() {
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
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

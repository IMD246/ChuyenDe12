package com.example.EnglishBeginner.DTO;

public class ReviewCourse {
    private String Question = "", correctAnswer = "", userAnswer = "", typeQuestion = "";
    private boolean check;

    public ReviewCourse() {
    }

    public ReviewCourse(String question, String correctAnswer, String userAnswer, String typeQuestion, boolean check) {
        Question = question;
        this.correctAnswer = correctAnswer;
        this.userAnswer = userAnswer;
        this.typeQuestion = typeQuestion;
        this.check = check;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getTypeQuestion() {
        return typeQuestion;
    }

    public void setTypeQuestion(String typeQuestion) {
        this.typeQuestion = typeQuestion;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}

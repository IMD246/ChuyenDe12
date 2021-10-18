package com.example.myapplication.Admin.LearnManagement;

public class TypeQuestion {
    private int id;
    private String typeQuestionName;

    public TypeQuestion(int id, String typeQuestionName) {
        this.id = id;
        this.typeQuestionName = typeQuestionName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeQuestionName() {
        return typeQuestionName;
    }

    public void setTypeQuestionName(String typeQuestionName) {
        this.typeQuestionName = typeQuestionName;
    }
}

package com.example.testapp2.DTO;

public class TypeQuestion {
    int id;
    String typeQuestionName;

    public TypeQuestion(int id, String typeQuestionName) {
        this.id = id;
        this.typeQuestionName = typeQuestionName;
    }

    public TypeQuestion() {
    }

    public int getId() {
        return id;
    }

    public String getTypeQuestionName() {
        return typeQuestionName;
    }

    public void setTypeQuestionName(String typeQuestionName) {
        this.typeQuestionName = typeQuestionName;
    }
}

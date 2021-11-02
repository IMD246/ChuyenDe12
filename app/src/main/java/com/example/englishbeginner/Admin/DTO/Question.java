package com.example.englishbeginner.Admin.DTO;

import com.example.englishbeginner.DEFAULTVALUE;

import java.io.Serializable;

public class Question implements Serializable {
    private int id,idTopic,idTypeQuestion;
    private String title,nameTopic,nameTypeQuestion,correctAnswer,
            example= DEFAULTVALUE.DEFAULTVALUE,word = DEFAULTVALUE.DEFAULTVALUE,
            typeWord = DEFAULTVALUE.DEFAULTVALUE,grammar= DEFAULTVALUE.DEFAULTVALUE;
    public Question(int id, int idTopic, int idTypeQuestion, String title, String nameTopic, String nameTypeQuestion,
                    String correctAnswer) {
        this.id = id;
        this.idTopic = idTopic;
        this.idTypeQuestion = idTypeQuestion;
        this.title = title;
        this.nameTopic = nameTopic;
        this.nameTypeQuestion = nameTypeQuestion;
        this.correctAnswer = correctAnswer;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTypeWord() {
        return typeWord;
    }

    public void setTypeWord(String typeWord) {
        this.typeWord = typeWord;
    }

    public String getGrammar() {
        return grammar;
    }

    public void setGrammar(String grammar) {
        this.grammar = grammar;
    }

    public Question() {
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdTopic() {
        return idTopic;
    }

    public void setIdTopic(int idTopic) {
        this.idTopic = idTopic;
    }

    public int getIdTypeQuestion() {
        return idTypeQuestion;
    }

    public void setIdTypeQuestion(int idTypeQuestion) {
        this.idTypeQuestion = idTypeQuestion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNameTopic() {
        return nameTopic;
    }

    public void setNameTopic(String nameTopic) {
        this.nameTopic = nameTopic;
    }

    public String getNameTypeQuestion() {
        return nameTypeQuestion;
    }

    public void setNameTypeQuestion(String nameTypeQuestion) {
        this.nameTypeQuestion = nameTypeQuestion;
    }
}

package com.example.EnglishBeginner.DTO;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Question implements Serializable {
    private int id,idTopic;
    private String title,nameTopic,nameTypeQuestion,correctAnswer,
            example= DEFAULTVALUE.DEFAULTVALUE,exampleMeaning=DEFAULTVALUE.DEFAULTVALUE,word = DEFAULTVALUE.DEFAULTVALUE,wordMeaning = DEFAULTVALUE.DEFAULTVALUE,
            typeWord = DEFAULTVALUE.DEFAULTVALUE,grammar= DEFAULTVALUE.DEFAULTVALUE,urlImage="";
    public Question(int id, int idTopic, String title, String nameTopic, String nameTypeQuestion,
                    String correctAnswer) {
        this.id = id;
        this.idTopic = idTopic;
        this.title = title;
        this.nameTopic = nameTopic;
        this.nameTypeQuestion = nameTypeQuestion;
        this.correctAnswer = correctAnswer;
    }

    public String getExampleMeaning() {
        return exampleMeaning;
    }

    public void setExampleMeaning(String exampleMeaning) {
        this.exampleMeaning = exampleMeaning;
    }

    public String getWordMeaning() {
        return wordMeaning;
    }

    public void setWordMeaning(String wordMeaning) {
        this.wordMeaning = wordMeaning;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
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

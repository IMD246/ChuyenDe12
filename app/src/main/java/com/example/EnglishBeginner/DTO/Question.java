package com.example.EnglishBeginner.DTO;

import java.io.Serializable;

public class Question implements Serializable {
    private int id,idTopic;
    private String title,nameTopic,correctAnswer,
            example= "",exampleMeaning="",word = "",wordMeaning = "",
            typeWord = "",categoryWord,grammar= "",urlImage="";

    public Question(int id, int idTopic, String title, String nameTopic, String correctAnswer, String example, String exampleMeaning, String word, String wordMeaning, String typeWord, String categoryWord, String grammar, String urlImage) {
        this.id = id;
        this.idTopic = idTopic;
        this.title = title;
        this.nameTopic = nameTopic;
        this.correctAnswer = correctAnswer;
        this.example = example;
        this.exampleMeaning = exampleMeaning;
        this.word = word;
        this.wordMeaning = wordMeaning;
        this.typeWord = typeWord;
        this.categoryWord = categoryWord;
        this.grammar = grammar;
        this.urlImage = urlImage;
    }

    public String getCategoryWord() {
        return categoryWord;
    }

    public void setCategoryWord(String categoryWord) {
        this.categoryWord = categoryWord;
    }

    public Question() {
    }

    public String getExampleMeaning() {
        return exampleMeaning;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
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

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
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
}

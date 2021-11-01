package com.example.myapplication.User.DTO;

public class Word {
    int id;
    String word;
    String htmlText;
    String description;
    String pronounce;
    String typeWord,meaning;

    public Word(int id, String word, String htmlText, String description, String pronounce, String typeWord, String meaning) {
        this.id = id;
        this.word = word;
        this.htmlText = htmlText;
        this.description = description;
        this.pronounce = pronounce;
        this.typeWord = typeWord;
        this.meaning = meaning;
    }

    public Word() {
    }

    public Word(int id, String word, String htmlText, String description, String pronounce) {
        this.id = id;
        this.word = word;
        this.htmlText = htmlText;
        this.description = description;
        this.pronounce = pronounce;
    }

    public String getTypeWord() {
        return typeWord;
    }

    public void setTypeWord(String typeWord) {
        this.typeWord = typeWord;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    public String getPronounce() {
        return pronounce;
    }

    public void setPronounce(String pronounce) {
        this.pronounce = pronounce;
    }

    public Word(int id,String word,String meaning) {

        this.word = word;
        this.meaning = meaning;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


}

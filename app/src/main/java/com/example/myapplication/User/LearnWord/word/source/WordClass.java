package com.example.myapplication.User.LearnWord.word.source;



public class WordClass {
    int id;
    String word;
    String htmlText;
    String description;
    String pronounce;

    public WordClass(int id, String word, String htmlText, String description, String pronounce) {
        this.id = id;
        this.word = word;
        this.htmlText = htmlText;
        this.description = description;
        this.pronounce = pronounce;

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

    public WordClass(int id, String word) {
        this.id = id;
        this.word = word;
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

package com.example.myapplication.User.LearnWord.practice.source;

import java.util.ArrayList;

public class PracticeClass {

    String correctWord,meaning;
    ArrayList<String> listItem;

    public PracticeClass(String word, String meaning, ArrayList<String> listItem) {
        this.correctWord = word;
        this.meaning = meaning;
        this.listItem = listItem;
    }

    public String getCorrectWord() {
        return correctWord;
    }

    public void setCorrectWord(String correctWord) {
        this.correctWord = correctWord;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public ArrayList<String> getListItem() {
        return listItem;
    }

    public void setListItem(ArrayList<String> listItem) {
        this.listItem = listItem;
    }

}

package com.example.myapplication.User.LearnWord.vocubulary;

public class VocubularyItem {

    private String wordText;
    private int wordImage;

    public String getWordText() {
        return wordText;
    }

    public void setWordText(String wordText) {
        this.wordText = wordText;
    }

    public int getWordImage() {
        return wordImage;
    }

    public void setWordImage(int wordImage) {
        this.wordImage = wordImage;
    }

    public VocubularyItem(String wordText, int wordImage) {
        this.wordText = wordText;
        this.wordImage = wordImage;
    }
}

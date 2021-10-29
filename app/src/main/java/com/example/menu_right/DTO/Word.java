package com.example.menu_right.DTO;

public class Word {
    private int id;
    private String word,typeWord,meaning;

    public Word(int id, String word, String typeWord, String meaning) {
        this.id = id;
        this.word = word;
        this.typeWord = typeWord;
        this.meaning = meaning;
    }

    public Word() {
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
}

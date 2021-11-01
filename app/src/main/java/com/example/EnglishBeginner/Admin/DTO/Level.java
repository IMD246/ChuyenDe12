package com.example.EnglishBeginner.Admin.DTO;

public class Level {
    private int id;
    private int nameLevel;
    private String urlImage = "";

    public Level(int id, int nameLevel, String urlImage) {
        this.id = id;
        this.nameLevel = nameLevel;
        this.urlImage = urlImage;
    }

    public Level() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNameLevel() {
        return nameLevel;
    }

    public void setNameLevel(int nameLevel) {
        this.nameLevel = nameLevel;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}

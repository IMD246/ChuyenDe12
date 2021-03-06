package com.example.EnglishBeginner.DTO;

public class Topic {
    private int id,idLevel,level;
    private String nameTopic,urlImage="";

    public Topic(int id, String nameTopic, String urlImage) {
        this.id = id;
        this.nameTopic = nameTopic;
        this.urlImage = urlImage;
    }

    public Topic(int id, int idLevel, int level, String nameTopic, String urlImage) {
        this.id = id;
        this.idLevel = idLevel;
        this.level = level;
        this.nameTopic = nameTopic;
        this.urlImage = urlImage;
    }

    public Topic() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdLevel() {
        return idLevel;
    }

    public void setIdLevel(int idLevel) {
        this.idLevel = idLevel;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getNameTopic() {
        return nameTopic;
    }

    public void setNameTopic(String nameTopic) {
        this.nameTopic = nameTopic;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }
}

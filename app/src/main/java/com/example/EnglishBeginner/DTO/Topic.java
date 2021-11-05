package com.example.EnglishBeginner.DTO;

public class Topic {
    private int id,idLevel=0,level=0;
    String nameTopic,urlImage="";

    public Topic(int id, String nameTopic, String urlImage) {
        this.id = id;
        this.nameTopic = nameTopic;
        this.urlImage = urlImage;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Topic() {
    }

    public int getLevel() {
        return level;
    }


    public int getId() {
        return id;
    }

    public int getIdLevel() {
        return idLevel;
    }

    public String getNameTopic() {
        return nameTopic;
    }

    public void setNameTopic(String nameTopic) {
        this.nameTopic = nameTopic;
    }
}

package com.example.myapplication.Admin.LearnManagement;

public class Topic {
    private int id,idLevel,level;
    String nameTopic,urlImageTopic;

    public Topic(int id, int idLevel, int level, String nameTopic, String urlImageTopic) {
        this.id = id;
        this.idLevel = idLevel;
        this.level = level;
        this.nameTopic = nameTopic;
        this.urlImageTopic = urlImageTopic;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public String getUrlImageTopic() {
        return urlImageTopic;
    }

    public void setNameTopic(String nameTopic) {
        this.nameTopic = nameTopic;
    }

    public void setUrlImageTopic(String urlImageTopic) {
        this.urlImageTopic = urlImageTopic;
    }
}

package com.example.myapplication.Admin.LearnManagement;

public class Topic {
    private int id,idLevel,level;
    String nameTopic;

    public Topic(int id, int idLevel, int level, String nameTopic) {
        this.id = id;
        this.idLevel = idLevel;
        this.level = level;
        this.nameTopic = nameTopic;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Topic() {
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

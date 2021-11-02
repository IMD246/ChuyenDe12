package com.example.englishbeginner.DTO;

import java.util.ArrayList;
import java.util.List;

public class Level {
    int id;
    int nameLevel;
    String urlImage = "";
    List<Topic> listTopic = new ArrayList<>();
    public Level(int id, int nameLevel) {
        this.id = id;
        this.nameLevel = nameLevel;
    }

    public Level(int id, int nameLevel, String urlImage) {
        this.id = id;
        this.nameLevel = nameLevel;
        this.urlImage = urlImage;
    }
    public Level() {
    }

    public Level(int id, int nameLevel, String urlImage, List<Topic> listTopic) {
        this.id = id;
        this.nameLevel = nameLevel;
        this.urlImage = urlImage;
        this.listTopic = listTopic;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getId() {
        return id;
    }

    public int getNameLevel() {
        return nameLevel;
    }

    public void setNameLevel(int nameLevel) {
        this.nameLevel = nameLevel;
    }

    public List<Topic> getListTopic() {
        return listTopic;
    }

    public void setListTopic(List<Topic> listTopic) {
        this.listTopic = listTopic;
    }
}

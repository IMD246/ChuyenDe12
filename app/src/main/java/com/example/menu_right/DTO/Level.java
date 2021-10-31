package com.example.menu_right.DTO;

import com.example.menu_right.Adapter.Topic_Adapter;
import com.example.menu_right.DAO.DAOTopic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Level {
    int id;
    int nameLevel;
    String urlImage = "";
    List<Topic> listtopic = new ArrayList<>();
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

    public Level(int id, int nameLevel, String urlImage, List<Topic> listtopic) {
        this.id = id;
        this.nameLevel = nameLevel;
        this.urlImage = urlImage;
        this.listtopic = listtopic;
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

    public List<Topic> getListtopic() {
        return listtopic;
    }

    public void setListtopic(List<Topic> listtopic) {
        this.listtopic = listtopic;
    }
}

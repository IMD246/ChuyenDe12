package com.example.EnglishBeginner.DTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Level {
    int id;
    int nameLevel;
    String urlImage = "";
    //HashMap<String,Topic>hashMap = new HashMap<>();
//    List<Topic> listtopic;
    public Level(int id, int nameLevel) {
//        listtopic = new ArrayList<>();
        this.id = id;
        this.nameLevel = nameLevel;
    }

    public Level(int id, int nameLevel, String urlImage) {
//        listtopic = new ArrayList<>();
        this.id = id;
        this.nameLevel = nameLevel;
        this.urlImage = urlImage;
    }
    public Level() {
//        listtopic = new ArrayList<>();
    }

//    public Level(int id, int nameLevel, String urlImage,HashMap<String,Topic>hashMap) {
//        this.id = id;
//        this.nameLevel = nameLevel;
//        this.urlImage = urlImage;
////        this.listtopic = listtopic;
//        this.hashMap = hashMap;
//    }

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

//    public List<Topic> getListtopic() {
//        return listtopic;
//    }
//
//    public void setListtopic(List<Topic> listtopic) {
//        this.listtopic = listtopic;
//    }

//    public HashMap<String, Topic> getHashMap() {
//        return hashMap;
//    }
//
//    public void setHashMap(HashMap<String, Topic> hashMap) {
//        this.hashMap = hashMap;
//    }
}

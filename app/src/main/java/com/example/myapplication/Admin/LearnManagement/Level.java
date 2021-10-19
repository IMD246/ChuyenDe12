package com.example.myapplication.Admin.LearnManagement;

public class Level {
    int id;
    int nameLevel;

    public Level(int id, int nameLevel) {
        this.id = id;
        this.nameLevel = nameLevel;
    }

    public Level() {
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
}

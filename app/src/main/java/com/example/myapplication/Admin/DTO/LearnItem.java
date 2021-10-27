package com.example.myapplication.Admin.DTO;

public class LearnItem {
    private String name;
    private int imageItem;

    public LearnItem(String name, int imageItem) {
        this.name = name;
        this.imageItem = imageItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageItem() {
        return imageItem;
    }

    public void setImageItem(int imageItem) {
        this.imageItem = imageItem;
    }
}

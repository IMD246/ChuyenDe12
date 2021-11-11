package com.example.EnglishBeginner.fragment.LearnWord.notification.source;

public class DayItem {

    int id;
    String day;
   int status;

    public DayItem(int id, String day, int status) {
        this.id = id;
        this.day = day;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

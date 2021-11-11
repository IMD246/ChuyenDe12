package com.example.EnglishBeginner.fragment.LearnWord.history;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class HistoryItem {
    String id;
    String word,time;

    public HistoryItem(String id, String word, String time) {
        this.id = id;
        this.word = word;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public static String getDateTimeNow(){
        DateFormat df = new SimpleDateFormat(" d MMM yyyy, HH:mm");
        String nowTime = df.format(Calendar.getInstance().getTime());
        return  nowTime;
    }
}

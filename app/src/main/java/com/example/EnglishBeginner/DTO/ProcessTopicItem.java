package com.example.EnglishBeginner.DTO;

public class ProcessTopicItem {
    private int process=0,progress=0,idTopic=0;

    public ProcessTopicItem(int process, int progress, int idTopic) {
        this.process = process;
        this.progress = progress;
        this.idTopic = idTopic;
    }

    public ProcessTopicItem() {
    }
    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getIdTopic() {
        return idTopic;
    }

    public void setIdTopic(int idTopic) {
        this.idTopic = idTopic;
    }
}

package com.example.EnglishBeginner.DTO;

public class ProcessTopicItem {
    private int idTopic,process;
    private String processTopic;

    public ProcessTopicItem(int idTopic, int process, String processTopic) {
        this.idTopic = idTopic;
        this.process = process;
        this.processTopic = processTopic;
    }

    public ProcessTopicItem() {
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public int getIdTopic() {
        return idTopic;
    }

    public void setIdTopic(int idTopic) {
        this.idTopic = idTopic;
    }

    public String getProcessTopic() {
        return processTopic;
    }

    public void setProcessTopic(String processTopic) {
        this.processTopic = processTopic;
    }
}

package com.example.englishbeginner.Admin.DTO;

public class QuestionLearn {
    private int idQuestion,idTopic;
    private String title,word,example,grammar;

    public QuestionLearn(int idQuestion, int idTopic, String title, String word, String example, String grammar) {
        this.idQuestion = idQuestion;
        this.idTopic = idTopic;
        this.title = title;
        this.word = word;
        this.example = example;
        this.grammar = grammar;
    }

    public QuestionLearn() {
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public int getIdTopic() {
        return idTopic;
    }

    public void setIdTopic(int idTopic) {
        this.idTopic = idTopic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getGrammar() {
        return grammar;
    }

    public void setGrammar(String grammar) {
        this.grammar = grammar;
    }
}

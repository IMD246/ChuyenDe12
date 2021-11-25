package com.example.EnglishBeginner.DTO;

public class Blog {
    int id = -1;
    String idUser;
    String nameUser;
    String dayOfPost;
    String title;
    String content;
    String urlImage = "";
    boolean checkApply = false;
    int comment = 0;
    int like = 0;
    int view = 0;

    public Blog(int id, String idUser, String nameUser, String dayOfPost, String title, String content, String urlImage, boolean checkApply, int comment, int like, int view) {
        this.id = id;
        this.idUser = idUser;
        this.nameUser = nameUser;
        this.dayOfPost = dayOfPost;
        this.title = title;
        this.content = content;
        this.urlImage = urlImage;
        this.checkApply = checkApply;
        this.comment = comment;
        this.like = like;
        this.view = view;
    }

    public boolean isCheckApply() {
        return checkApply;
    }

    public void setCheckApply(boolean checkApply) {
        this.checkApply = checkApply;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public Blog() {
    }
    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDayOfPost() {
        return dayOfPost;
    }

    public void setDayOfPost(String dayOfPost) {
        this.dayOfPost = dayOfPost;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }
}

package com.example.myapplication.source;

public class Blog {

    String idUser;
    String dayOfPost;
    String title;
    String url_image;
    int comment;
    int like;
    int view;
    String typeMenu;

    public Blog(String idUser, String dayOfPost, String title, String url_image, int comment, int like, int view, String typeMenu) {
        this.idUser = idUser;
        this.dayOfPost = dayOfPost;
        this.title = title;
        this.url_image = url_image;
        this.comment = comment;
        this.like = like;
        this.view = view;
        this.typeMenu = typeMenu;
    }

    public String getTypeMenu() {
        return typeMenu;
    }

    public void setTypeMenu(String typeMenu) {
        this.typeMenu = typeMenu;
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

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
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

    public Blog(String idUser, String dayOfPost, String title, String url_image, int comment, int like, int view) {
        this.idUser = idUser;
        this.dayOfPost = dayOfPost;
        this.title = title;
        this.url_image = url_image;
        this.comment = comment;
        this.like = like;
        this.view = view;
    }

    public Blog() {
    }
}

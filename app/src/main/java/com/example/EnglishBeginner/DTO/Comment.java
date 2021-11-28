package com.example.EnglishBeginner.DTO;

public class Comment {
    int id = -1;
    String idUser, idBlog;
    String nameUser;
    String dayOfPost;
    String content;
    String urlImage = "";
    int like = 0;

    public Comment() {
    }

    public Comment(int id, String idUser, String idBlog, String nameUser, String dayOfPost, String content, String urlImage, int like) {
        this.id = id;
        this.idUser = idUser;
        this.idBlog = idBlog;
        this.nameUser = nameUser;
        this.dayOfPost = dayOfPost;
        this.content = content;
        this.urlImage = urlImage;
        this.like = like;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdBlog() {
        return idBlog;
    }

    public void setIdBlog(String idBlog) {
        this.idBlog = idBlog;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getDayOfPost() {
        return dayOfPost;
    }

    public void setDayOfPost(String dayOfPost) {
        this.dayOfPost = dayOfPost;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}

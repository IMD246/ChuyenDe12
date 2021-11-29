package com.example.EnglishBeginner.DTO;

public class SubComment {
    int id = -1;
    String idBlog, idComment, idUser;
    String dayOfPost;
    String content;
    int like = 0;

    public SubComment() {
    }

    public SubComment(int id, String idBlog, String idComment, String idUser, String dayOfPost, String content, int like) {
        this.id = id;
        this.idBlog = idBlog;
        this.idComment = idComment;
        this.idUser = idUser;
        this.dayOfPost = dayOfPost;
        this.content = content;
        this.like = like;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdBlog() {
        return idBlog;
    }

    public void setIdBlog(String idBlog) {
        this.idBlog = idBlog;
    }

    public String getIdComment() {
        return idComment;
    }

    public void setIdComment(String idComment) {
        this.idComment = idComment;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}

package com.example.EnglishBeginner.DTO;

public class Like {
    private int idBlog;
    private String idUser;

    public Like() {
    }

    public Like(int idBlog, String idUser) {
        this.idBlog = idBlog;
        this.idUser = idUser;
    }

    public int getIdBlog() {
        return idBlog;
    }

    public void setIdBlog(int idBlog) {
        this.idBlog = idBlog;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}

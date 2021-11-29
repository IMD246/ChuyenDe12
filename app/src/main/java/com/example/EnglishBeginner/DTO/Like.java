package com.example.EnglishBeginner.DTO;

public class Like {
    private boolean Checklike;
    private int idBlog;
    private String idUser;

    public Like() {
    }

    public Like(boolean checklike, int idBlog, String idUser) {
        Checklike = checklike;
        this.idBlog = idBlog;
        this.idUser = idUser;
    }

    public boolean isChecklike() {
        return Checklike;
    }

    public void setChecklike(boolean checklike) {
        Checklike = checklike;
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

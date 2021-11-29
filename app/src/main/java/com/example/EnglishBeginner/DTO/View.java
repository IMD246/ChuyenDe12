package com.example.EnglishBeginner.DTO;

public class View {
    private boolean CheckView;
    private int idBlog;
    private String idUser;

    public View() {
    }

    public View(boolean checkView, int idBlog, String idUser) {
        CheckView = checkView;
        this.idBlog = idBlog;
        this.idUser = idUser;
    }

    public boolean isChecklike() {
        return CheckView;
    }

    public void setCheckView(boolean checkView) {
        CheckView = checkView;
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

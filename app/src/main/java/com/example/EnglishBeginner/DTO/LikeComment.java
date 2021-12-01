package com.example.EnglishBeginner.DTO;

public class LikeComment {
    private int idComment;
    private String idUser;

    public LikeComment() {
    }

    public LikeComment(int idComment, String idUser) {
        this.idComment = idComment;
        this.idUser = idUser;
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }
}

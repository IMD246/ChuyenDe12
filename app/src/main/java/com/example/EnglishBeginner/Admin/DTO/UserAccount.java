package com.example.EnglishBeginner.Admin.DTO;

public class UserAccount {
    private String documentID,email,authenticate;
    private Boolean isBlock = false;
    public UserAccount(String email, Boolean isBlock) {
        this.email = email;
        this.isBlock = isBlock;
    }

    public UserAccount() {
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getBlock() {
        return isBlock;
    }

    public String getAuthenticate() {
        return authenticate;
    }

    public void setBlock(Boolean block) {
        isBlock = block;
    }
}

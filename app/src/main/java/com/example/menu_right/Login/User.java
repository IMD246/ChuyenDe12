package com.example.menu_right.Login;

public class User {
    private String fullname,email;
    private int age,expPerDay,authenticate,totalExp,idTypeProceedPerDay;
    private String imageUser;
    private boolean isActivePrivacy;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getExpPerDay() {
        return expPerDay;
    }


    public int getTotalExp() {
        return totalExp;
    }

    public int getIdTypeProceedPerDay() {
        return idTypeProceedPerDay;
    }

    public String getImageUser() {
        return imageUser;
    }

    public boolean isActivePrivacy() {
        return isActivePrivacy;
    }


    public void setExpPerDay(int expPerDay) {
        this.expPerDay = expPerDay;
    }

    public void setTotalExp(int totalExp) {
        this.totalExp = totalExp;
    }

    public void setIdTypeProceedPerDay(int idTypeProceedPerDay) {
        this.idTypeProceedPerDay = idTypeProceedPerDay;
    }

    public void setImageUser(String imageUser) {
        this.imageUser = imageUser;
    }

    public void setActivePrivacy(boolean activePrivacy) {
        isActivePrivacy = activePrivacy;
    }

    public User() {
    }

    public User(String fullname, String email, int age, int expPerDay, int totalExp, int idTypeProceedPerDay, String imageUser, boolean isActivePrivacy) {
        this.fullname = fullname;
        this.email = email;
        this.age = age;
        this.expPerDay = expPerDay;
        this.totalExp = totalExp;
        this.idTypeProceedPerDay = idTypeProceedPerDay;
        this.imageUser = imageUser;
        this.isActivePrivacy = isActivePrivacy;
    }
}

package com.example.EnglishBeginner.DTO;

public class User {
    private String fullname,email = "",gender = "";
    private int age,expPerDay,totalExp,idTypeProceedPerDay;
    private String imageUser = "";
    private boolean activePrivacy;

    public User(String fullname, String email, String gender, int age, int expPerDay, int totalExp, int idTypeProceedPerDay, String imageUser, boolean activePrivacy) {
        this.fullname = fullname;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.expPerDay = expPerDay;
        this.totalExp = totalExp;
        this.idTypeProceedPerDay = idTypeProceedPerDay;
        this.imageUser = imageUser;
        this.activePrivacy = activePrivacy;
    }

    public User() {
    }



    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
        return activePrivacy;
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
        this.activePrivacy = activePrivacy;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                '}';
    }
}

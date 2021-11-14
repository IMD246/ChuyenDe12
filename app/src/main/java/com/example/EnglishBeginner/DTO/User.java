package com.example.EnglishBeginner.DTO;

public class User {
    private String fullname,email = "",gender = "",keyPass= "",passWord= "";
    private int age,expPerDay,totalExp,idTypeProceedPerDay,countLogin = 0;
    private String imageUser = "";
    private boolean activePrivacy;

    public User(String fullname, String email, String gender, String keyPass, int age, int expPerDay, int totalExp, int idTypeProceedPerDay, String imageUser, boolean activePrivacy) {
        this.fullname = fullname;
        this.email = email;
        this.gender = gender;
        this.keyPass = keyPass;
        this.age = age;
        this.expPerDay = expPerDay;
        this.totalExp = totalExp;
        this.idTypeProceedPerDay = idTypeProceedPerDay;
        this.imageUser = imageUser;
        this.activePrivacy = activePrivacy;
    }

    public int getCountLogin() {
        return countLogin;
    }

    public void setCountLogin(int countLogin) {
        this.countLogin = countLogin;
    }

    public User() {
    }

    public String getKeyPass() {
        return keyPass;
    }

    public void setKeyPass(String keyPass) {
        this.keyPass = keyPass;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
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

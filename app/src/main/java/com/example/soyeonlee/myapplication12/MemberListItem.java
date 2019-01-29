package com.example.soyeonlee.myapplication12;

public class MemberListItem {

    private String userImage;
    private String userName;
    private String userNickname;
    private String userPhone;
    private String userDate;
    private String userBirth;
    private String userGender;
    private String userID;
    private String userPassword;

    public MemberListItem(String userImage, String userName, String userNickname, String userPhone) {
        this.userImage = userImage;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userPhone = userPhone;
    }

    public MemberListItem(String userDate, String userImage, String userName, String userNickname, String userBirth,
                          String userPhone, String userGender, String userID, String userPassword) {
        this.userDate = userDate;
        this.userImage = userImage;
        this.userName = userName;
        this.userNickname = userNickname;
        this.userPhone = userPhone;
        this.userBirth = userBirth;
        this.userGender = userGender;
        this.userID = userID;
        this.userPassword = userPassword;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        this.userDate = userDate;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        this.userBirth = userBirth;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}

package com.example.soyeonlee.myapplication12;

public class LoginUserInfo {
    private static String userID;
    private static String userPassword;
    private static String userName;
    private static String userBirth;
    private static String userPhone;
    private static String userNickname;
    private static String userImage;
    private static String userDate;

    public static String getUserID() {
        return userID;
    }

    public static void setUserID(String userID) {
        LoginUserInfo.userID = userID;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        LoginUserInfo.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        LoginUserInfo.userName = userName;
    }

    public String getUserBirth() {
        return userBirth;
    }

    public void setUserBirth(String userBirth) {
        LoginUserInfo.userBirth = userBirth;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        LoginUserInfo.userPhone = userPhone;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        LoginUserInfo.userNickname = userNickname;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        LoginUserInfo.userImage = userImage;
    }

    public String getUserDate() {
        return userDate;
    }

    public void setUserDate(String userDate) {
        LoginUserInfo.userDate = userDate;
    }
}

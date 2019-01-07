package com.example.soyeonlee.myapplication12;

import android.widget.Button;

public class ListItem {

    private String userImage;
    private String userName;
    private String date;
    private String writing;
    private String image;
    private Button like;
    private Button comment;

    public ListItem(String userImage, String userName, String date, String writing, String image) {
        this.userImage = userImage;
        this.userName = userName;
        this.date = date;
        this.writing = writing;
        this.image = image;
    }

    public ListItem(String date, String writing, String image) {
        this.date = date;
        this.writing = writing;
        this.image = image;
    }

    public ListItem(String userImage, String userName) {
        this.userImage = userImage;
        this.userName = userName;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWriting() {
        return writing;
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Button getLike() {
        return like;
    }

    public void setLike(Button like) {
        this.like = like;
    }

    public Button getComment() {
        return comment;
    }

    public void setComment(Button comment) {
        this.comment = comment;
    }
}

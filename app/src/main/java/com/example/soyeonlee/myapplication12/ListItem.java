package com.example.soyeonlee.myapplication12;

import android.widget.Button;
import android.widget.GridLayout;

public class ListItem {

    private String userImage;
    private String userName;
    private String date;
    private String text;
    private String image;
    private String video;
    private Button like;
    private Button comment;
    private String[] images;
    private String[] videos;
    private GridLayout gridLayout;

    public ListItem(String text, GridLayout gridLayout) {
        this.text = text;
        this.gridLayout = gridLayout;
    }

    public ListItem(String[] images, String[] videos) {
        this.images = images;
        this.videos = videos;
    }

    public ListItem(String userImage, String userName, String date, String text, String image) {
        this.userImage = userImage;
        this.userName = userName;
        this.date = date;
        this.text = text;
        this.image = image;
    }

    public ListItem(String text, String image, String video) {
        this.text = text;
        this.image = image;
        this.video = video;
    }

    public ListItem(String text) {
        this.text = text;
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

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String[] getVideos() {
        return videos;
    }

    public void setVideos(String[] videos) {
        this.videos = videos;
    }
}

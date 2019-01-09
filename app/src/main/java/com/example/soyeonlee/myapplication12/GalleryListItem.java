package com.example.soyeonlee.myapplication12;

public class GalleryListItem {

    private String galleryImage;
    private String galleryTitle;
    private String galleryNum;
    private String galleryPath;

    public GalleryListItem(String galleryImage, String galleryTitle, String galleryNum, String galleryPath) {
        this.galleryImage = galleryImage;
        this.galleryTitle = galleryTitle;
        this.galleryNum = galleryNum;
        this.galleryPath = galleryPath;
    }

    public String getGalleryImage() {
        return galleryImage;
    }

    public void setGalleryImage(String galleryImage) {
        this.galleryImage = galleryImage;
    }

    public String getGalleryTitle() {
        return galleryTitle;
    }

    public void setGalleryTitle(String galleryTitle) {
        this.galleryTitle = galleryTitle;
    }

    public String getGalleryNum() {
        return galleryNum;
    }

    public void setGalleryNum(String galleryNum) {
        this.galleryNum = galleryNum;
    }

    public String getGalleryPath() {
        return galleryPath;
    }

    public void setGalleryPath(String galleryPath) {
        this.galleryPath = galleryPath;
    }
}

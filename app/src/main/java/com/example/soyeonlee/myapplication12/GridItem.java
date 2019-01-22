package com.example.soyeonlee.myapplication12;

import android.net.Uri;

public class GridItem {
    private String gridImage;
    private String gridImageUri;


    public GridItem(String gridImage) {
        this.gridImage = gridImage;
    }

    /*
    public GridItem(String gridImageUri) {
        this.gridImageUri = gridImageUri;
    }*/

    public String getGridImage() {
        return gridImage;
    }

    public void setGridImage(String gridImage) {
        this.gridImage = gridImage;
    }

    public String getGridImageUri() {
        return gridImageUri;
    }

    public void setGridImageUri(String gridImageUri) {
        this.gridImageUri = gridImageUri;
    }
}

package com.example.soyeonlee.myapplication12;

public class MapItem {

    private String mapName;
    private String mapAddress;

    public MapItem(String mapName, String mapAddress) {
        this.mapName = mapName;
        this.mapAddress = mapAddress;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getMapAddress() {
        return mapAddress;
    }

    public void setMapAddress(String mapAddress) {
        this.mapAddress = mapAddress;
    }
}

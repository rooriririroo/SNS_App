package com.example.soyeonlee.myapplication12;

public class FileListItem {

    private String fileName;
    private String filePath;
    private boolean isDirectory;
    private boolean isSelected;

    public FileListItem(String fileName, String filePath, boolean isDirectory) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.isDirectory = isDirectory;
    }

    public FileListItem(String fileName, String filePath, boolean isDirectory, boolean isSelected) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.isDirectory = isDirectory;
        this.isSelected = isSelected;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean getIsDirectory() {
        return isDirectory;
    }

    public void setIsDirectory(boolean directory) {
        isDirectory = directory;
    }

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean selected) {
        isSelected = selected;
    }
}

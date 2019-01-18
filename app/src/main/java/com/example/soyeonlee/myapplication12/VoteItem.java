package com.example.soyeonlee.myapplication12;

import java.util.ArrayList;

public class VoteItem {
    private String voteTitle;
    private ArrayList<String> voteContent;
    private boolean isPlural;
    private boolean isAnonymity;
    private boolean isAvaliable;

    public VoteItem(String voteTitle, ArrayList<String> voteContent, boolean isPlural, boolean isAnonymity, boolean isAvaliable) {
        this.voteTitle = voteTitle;
        this.voteContent = voteContent;
        this.isPlural = isPlural;
        this.isAnonymity = isAnonymity;
        this.isAvaliable = isAvaliable;
    }

    public String getVoteTitle() {
        return voteTitle;
    }

    public void setVoteTitle(String voteTitle) {
        this.voteTitle = voteTitle;
    }

    public ArrayList<String> getVoteContent() {
        return voteContent;
    }

    public void setVoteContent(ArrayList<String> voteContent) {
        this.voteContent = voteContent;
    }

    public boolean isPlural() {
        return isPlural;
    }

    public void setPlural(boolean plural) {
        isPlural = plural;
    }

    public boolean isAnonymity() {
        return isAnonymity;
    }

    public void setAnonymity(boolean anonymity) {
        isAnonymity = anonymity;
    }

    public boolean isAvaliable() {
        return isAvaliable;
    }

    public void setAvaliable(boolean avaliable) {
        isAvaliable = avaliable;
    }
}

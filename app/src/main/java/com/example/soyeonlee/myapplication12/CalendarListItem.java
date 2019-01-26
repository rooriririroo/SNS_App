package com.example.soyeonlee.myapplication12;

public class CalendarListItem {
    private String calendarDate;
    private String calendarDay;
    private String calendarTitle;
    private String calendarTime;
    private String calendarMap;

    public CalendarListItem(String calendarDate, String calendarDay, String calendarTitle, String calendarTime, String calendarMap) {
        this.calendarDate = calendarDate;
        this.calendarDay = calendarDay;
        this.calendarTitle = calendarTitle;
        this.calendarTime = calendarTime;
        this.calendarMap = calendarMap;
    }

    public String getCalendarDate() {
        return calendarDate;
    }

    public void setCalendarDate(String calendarDate) {
        this.calendarDate = calendarDate;
    }

    public String getCalendarDay() {
        return calendarDay;
    }

    public void setCalendarDay(String calendarDay) {
        this.calendarDay = calendarDay;
    }

    public String getCalendarTitle() {
        return calendarTitle;
    }

    public void setCalendarTitle(String calendarTitle) {
        this.calendarTitle = calendarTitle;
    }

    public String getCalendarTime() {
        return calendarTime;
    }

    public void setCalendarTime(String calendarTime) {
        this.calendarTime = calendarTime;
    }

    public String getCalendarMap() {
        return calendarMap;
    }

    public void setCalendarMap(String calendarMap) {
        this.calendarMap = calendarMap;
    }
}

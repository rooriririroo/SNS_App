package com.example.soyeonlee.myapplication12;

public class CalendarListItem {
    private String calendarDate;
    private String calendarDay;
    private String calendarTitle;
    private String calendarTime;
    private String calendarMap;
    private String sub;
    private String allDay;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String repeated;
    private String alarm;
    private String mapAddress;

    public CalendarListItem(String calendarDate, String calendarDay, String calendarTitle, String calendarTime, String calendarMap,
                            String sub, String allDay, String startDate, String startTime, String endDate, String endTime, String repeated, String alarm, String mapAddress) {
        this.calendarDate = calendarDate;
        this.calendarDay = calendarDay;
        this.calendarTitle = calendarTitle;
        this.calendarTime = calendarTime;
        this.calendarMap = calendarMap;
        this.sub = sub;
        this.allDay = allDay;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.repeated = repeated;
        this.alarm = alarm;
        this.mapAddress = mapAddress;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    public String getMapAddress() {
        return mapAddress;
    }

    public void setMapAddress(String mapAddress) {
        this.mapAddress = mapAddress;
    }
}

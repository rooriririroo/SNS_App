package com.example.soyeonlee.myapplication12;

public class CalendarListItem {
    private String calendarDate;
    private String calendarDay;
    private String calendarName;
    private String calendarText;
    private String calendarBirth;

    public CalendarListItem(String calendarDate, String calendarDay, String calendarName) {
        this.calendarDate = calendarDate;
        this.calendarDay = calendarDay;
        this.calendarName = calendarName;
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

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public String getCalendarText() {
        return calendarText;
    }

    public void setCalendarText(String calendarText) {
        this.calendarText = calendarText;
    }

    public String getCalendarBirth() {
        return calendarBirth;
    }

    public void setCalendarBirth(String calendarBirth) {
        this.calendarBirth = calendarBirth;
    }
}

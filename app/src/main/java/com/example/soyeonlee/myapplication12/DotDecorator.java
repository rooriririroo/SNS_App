package com.example.soyeonlee.myapplication12;

import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

public class DotDecorator implements DayViewDecorator {
    private int color;
    private CalendarDay date;

    public DotDecorator(int year, int month, int day){
        date = CalendarDay.from(year,month,day);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(5, Color.rgb(156,39,176)));
    }

}

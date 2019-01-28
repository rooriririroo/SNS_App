package com.example.soyeonlee.myapplication12;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    //CalendarView calendarView;
    MaterialCalendarView calendarView;
    ListView calender_list;
    ArrayList<CalendarListItem> calendarListItemArrayList;
    CalendarListItemAdapter adapter;
    Calendar calendar;


    int selectYear;
    int selectMonth;
    int selectDay;

    int year;
    int month;
    int day;

    String mapName = "";
    String mapAddress = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar,container,false);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        calender_list = rootView.findViewById(R.id.calender_list);
        calendarListItemArrayList = new ArrayList<CalendarListItem>();
        adapter = new CalendarListItemAdapter(getContext(),calendarListItemArrayList);
        calender_list.setAdapter(adapter);
        calender_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScheduleDialog dialog = new ScheduleDialog(getActivity());
                dialog.show();
            }
        });

        load_schedule(year,month);

        calendarView = rootView.findViewById(R.id.calendarView);
        calendarView.state().edit().setMinimumDate(CalendarDay.from(2018,0,1)).commit();
        calendarView.setDateSelected(CalendarDay.from(year,month,day),true);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull final CalendarDay date, boolean selected) {
                calendarView.setDateSelected(CalendarDay.from(year,month,day),false);
                if(String.valueOf(date.getYear()).equals(String.valueOf(year))
                        && String.valueOf(date.getMonth()).equals(String.valueOf(month))
                        && String.valueOf(date.getDay()).equals(String.valueOf(day))) {
                    calendarView.setDateSelected(CalendarDay.from(year,month,day),true);
                }

                final String strDate = String.format(Locale.KOREA,"%d월 %d일",date.getMonth()+1,date.getDay());
                calendarListItemArrayList.clear();
                selectYear = date.getYear();
                selectMonth = date.getMonth();
                selectDay = date.getDay();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JsonParser parser = new JsonParser();
                            JsonObject jsonResponse = (JsonObject) parser.parse(response);
                            JsonArray array = (JsonArray) jsonResponse.get("response");
                            for(int i = 0; i<array.size(); i++) {
                                JsonObject object = (JsonObject) array.get(i);

                                String title = object.get("title").getAsString();
                                String sub = object.get("sub").getAsString();
                                String allDay = object.get("allDay").getAsString();
                                String startDate = object.get("startDate").getAsString();
                                String endDate = object.get("endDate").getAsString();

                                String startTime = "";
                                String endTime = "";

                                if(object.get("allDay").getAsString().equals("하루종일")) {
                                    startTime = "하루종일";
                                }

                                if(!object.get("startTime").getAsString().equals("")) {
                                    startTime = object.get("startTime").getAsString();
                                }

                                if(!object.get("endTime").getAsString().equals("")) {
                                    endTime = " ~ " + object.get("endTime").getAsString();
                                }

                                String repeated = object.get("repeated").getAsString();
                                String alarm = object.get("alarm").getAsString();

                                String map = "";
                                if(!object.get("map").getAsString().equals("")) {
                                    map = object.get("map").getAsString();
                                    if(map.contains(",")) {
                                        String[] maps = map.split(",");
                                        mapName = "  | " + maps[0].substring(1,maps[0].length());
                                        mapAddress = maps[1].trim().substring(0,maps[1].length()-1);
                                    }
                                    else {
                                        mapAddress = map.substring(1,map.length()-1);
                                    }
                                }

                                if(startDate.contains(strDate)) {
                                    int yearID = startDate.indexOf("년");
                                    String findMonth = startDate.substring(yearID+1);

                                    int monthID = findMonth.indexOf("월");
                                    String sMonth = findMonth.substring(1,monthID);
                                    String findDay = findMonth.substring(monthID+1);

                                    int dayID = findDay.indexOf("일");
                                    final String sDay = findDay.substring(1,dayID);

                                    calendarListItemArrayList.add(new CalendarListItem(sDay,getWeek(year,Integer.valueOf(sMonth),Integer.valueOf(sDay)) + "요일",
                                            title,startTime + endTime, mapName, sub, allDay, startDate, startTime, endDate, endTime, repeated, alarm, mapAddress));
                                }
                                adapter.notifyDataSetChanged();
                                startTime = "";
                                mapName = "";
                                mapAddress = "";
                            }

                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoadScheduleRequest loadScheduleRequest = new LoadScheduleRequest(responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(loadScheduleRequest);
            }
        });


        calendarView.setOnMonthChangedListener(new OnMonthChangedListener() {
            @Override
            public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
                load_schedule(date.getYear(),date.getMonth());
            }
        });

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int curId = menuItem.getItemId();
        if(curId == R.id.menu_plus) {
            Intent intent = new Intent(getActivity(),ScheduleActivity.class);
            intent.putExtra("year",selectYear);
            intent.putExtra("month",selectMonth+1);
            intent.putExtra("day",selectDay);
            intent.putExtra("week",getWeek(selectYear,selectMonth,selectDay));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public String getWeek(int year, int month, int dayOfMonth) {
        String week = "";

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DATE,dayOfMonth);

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1 :
                week = "일";
                return week;
            case 2 :
                week = "월";
                return week;
            case 3 :
                week = "화";
                return week;
            case 4 :
                week = "수";
                return week;
            case 5 :
                week = "목";
                return week;
            case 6 :
                week = "금";
                return week;
            case 7 :
                week = "토";
                return week;
        }
        return null;
    }

    public void load_schedule(final int year, final int month) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JsonParser parser = new JsonParser();
                    JsonObject jsonResponse = (JsonObject) parser.parse(response);
                    JsonArray array = (JsonArray) jsonResponse.get("response");
                    for(int i = 0; i<array.size(); i++) {
                        JsonObject object = (JsonObject) array.get(i);

                        String startDate = object.get("startDate").getAsString();
                        int yearID = startDate.indexOf("년");
                        String sYear = startDate.substring(0,yearID);
                        String findMonth = startDate.substring(yearID+1);

                        int monthID = findMonth.indexOf("월");
                        String sMonth = findMonth.substring(1,monthID);
                        String findDay = findMonth.substring(monthID+1);

                        int dayID = findDay.indexOf("일");
                        String sDay = findDay.substring(1,dayID);


                        calendar.set(year,month,Integer.valueOf(sDay));
                        Log.d("[Current Calendar]=>",String.valueOf(year)+String.valueOf(month)+sDay);
                        Log.d("[Database Calendar]=>",sYear + sMonth + sDay);

                        if((month+1) == Integer.valueOf(sMonth)) {
                            for(int j = 0; j<calendar.getActualMaximum(Calendar.DAY_OF_MONTH); j++) {
                                if(j == Integer.valueOf(sDay)) {
                                    calendarView.addDecorator(new DotDecorator(year,month,Integer.valueOf(sDay)));
                                }
                            }
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        LoadScheduleRequest loadScheduleRequest = new LoadScheduleRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(loadScheduleRequest);
    }
}

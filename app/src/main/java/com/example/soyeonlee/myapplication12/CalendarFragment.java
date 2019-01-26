package com.example.soyeonlee.myapplication12;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CalendarFragment extends Fragment {

    CalendarView calendarView;
    ListView calender_list;
    ArrayList<CalendarListItem> calendarListItemArrayList;
    CalendarListItemAdapter adapter;


    int selectYear;
    int selectMonth;
    int selectDay;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar,container,false);

        int year = Calendar.YEAR;
        int month = Calendar.MONTH;
        int day = Calendar.DAY_OF_MONTH;

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
        calendarView = rootView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, final int year, final int month, final int dayOfMonth) {
                final String strDate = String.format(Locale.KOREA,"%d월 %d일",month+1,dayOfMonth);
                calendarListItemArrayList.clear();
                selectYear = year;
                selectMonth = month;
                selectDay = dayOfMonth;
                Toast.makeText(getContext(),String.valueOf(month+1)+String.valueOf(dayOfMonth)+getWeek(year,month,dayOfMonth),Toast.LENGTH_SHORT).show();
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

                                String startTime = "";
                                if(!object.get("startTime").getAsString().equals("")) {
                                    startTime = object.get("startTime").getAsString();
                                }

                                String endDate = object.get("endDate").getAsString();

                                String endTime = "";
                                if(!object.get("endTime").getAsString().equals("")) {
                                    endTime = " ~ " + object.get("endTime").getAsString();
                                }

                                String alarm = object.get("alarm").getAsString();

                                String map = "";
                                if(!object.get("map").getAsString().equals("")) {
                                    map = "  |" + object.get("map").getAsString();
                                }

                                if(startDate.contains(strDate)) {
                                    int monthID = startDate.indexOf("월");
                                    String findDay = startDate.substring(monthID+1);

                                    int dayID = findDay.indexOf("일");
                                    final String sDay = findDay.substring(0,dayID);

                                    calendarListItemArrayList.add(new CalendarListItem(sDay,getWeek(year,month,dayOfMonth) + "요일",
                                            title,startTime + endTime,map));
                                }
                            }
                            adapter.notifyDataSetChanged();
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
}

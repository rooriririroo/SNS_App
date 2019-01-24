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
        calendarView = rootView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, final int year, final int month, final int dayOfMonth) {
                final String strDate = String.format(Locale.KOREA,"%02d월%02d일",month+1,dayOfMonth);
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

                                String userName = object.get("userName").getAsString();
                                String userBirth = object.get("userBirth").getAsString();

                                if(userBirth.contains(strDate)) {
                                    int yearID = userBirth.indexOf("년");
                                    String sYear = userBirth.substring(0,yearID);
                                    String findMonth = userBirth.substring(yearID+1);

                                    int monthID = findMonth.indexOf("월");
                                    String sMonth = findMonth.substring(0,monthID);
                                    String findDay = findMonth.substring(monthID+1);

                                    int dayID = findDay.indexOf("일");
                                    final String sDay = findDay.substring(0,dayID);

                                    calendarListItemArrayList.add(new CalendarListItem(sDay,getWeek(year,month,dayOfMonth),userName));
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoadMemberRequest loadMemberRequest = new LoadMemberRequest(responseListener);
                RequestQueue queue = Volley.newRequestQueue(getContext());
                queue.add(loadMemberRequest);
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

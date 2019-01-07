package com.example.soyeonlee.myapplication12;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

public class CalendarFragment extends Fragment {

    CalendarView calendarView;
    ListView calender_list;
    ArrayList<CalendarListItem> calendarListItemArrayList;
    CalendarListItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_calendar,container,false);

        int year = Calendar.YEAR;
        int month = Calendar.MONTH;
        int day = Calendar.DAY_OF_MONTH;

        calendarView = rootView.findViewById(R.id.calendarView);

        calender_list = rootView.findViewById(R.id.calender_list);
        calendarListItemArrayList = new ArrayList<CalendarListItem>();
        adapter = new CalendarListItemAdapter(getContext(),calendarListItemArrayList);
        calender_list.setAdapter(adapter);
        calendarListItemArrayList.add(new CalendarListItem("28","목요일","라이언"));
        calendarListItemArrayList.add(new CalendarListItem("29","금요일","무지"));

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
            Intent intent = new Intent(getContext(),WriteActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(menuItem);
    }
}
